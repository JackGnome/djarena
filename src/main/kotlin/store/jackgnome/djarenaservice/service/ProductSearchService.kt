package store.jackgnome.djarenaservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector
import org.hibernate.search.engine.search.sort.dsl.SortOrder
import org.hibernate.search.mapper.orm.Search
import org.hibernate.search.util.common.data.RangeBoundInclusion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import store.jackgnome.djarenaservice.mapper.productMapper
import store.jackgnome.djarenaservice.model.product.ProductEntity
import store.jackgnome.djarenaservice.model.product.ProductSearchDto
import store.jackgnome.djarenaservice.model.search.FilterParameter

@Service
class ProductSearchService : ApplicationListener<ApplicationReadyEvent> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var entityManager: EntityManager

    fun search(searchQuery: String, pageable: Pageable, filters: Map<String, String>): Page<ProductSearchDto> {
        val filterParams = getFilters(filters)
        logger.info { filterParams.toString() }

        val searchSession = Search.session(entityManager)
        val result = searchSession.search(ProductEntity::class.java)
            .where { f, root ->
                if (searchQuery.isEmpty() && filterParams.isEmpty()) {
                    root.add(f.matchAll())
                }

                if (searchQuery.isNotBlank()) {
                    root.add(f.match().fields("name").matching(searchQuery))
                }

                for (filter in filterParams) {
                    when (filter.type) {
                        FilterParameter.FilterType.RangeFilter -> applyRangeFilter(filter, f, root)
                        FilterParameter.FilterType.ValueFilter -> applyValueFilter(filter, f, root)
                        FilterParameter.FilterType.UnknownFilter -> {}
                    }
                }
            }
            .sort { f ->
                f.composite { b ->
                    run {
                        for (order in pageable.sort) {
                            b.add(
                                f.field(order.property)
                                    .order((if (order.isAscending) SortOrder.ASC else SortOrder.DESC))
                            )
                        }
                    }
                }
            }
            .fetch(pageable.offset.toInt(), pageable.pageSize)

        val products: List<ProductSearchDto> = result.hits()
            .map { r -> r as ProductEntity }
            .map { r -> productMapper.toSearchDto(r) }
            .toList()

        return PageImpl(products, pageable, result.total().hitCount())
    }

    @Transactional
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        try {
            val searchSession = Search.session(entityManager)
            val indexer = searchSession.massIndexer(ProductEntity::class.java)
            indexer.startAndWait()
        } catch (e: InterruptedException) {
            println("An error occurred trying to build the search index: $e")
        }
    }

    private fun applyRangeFilter(
        filter: FilterParameter,
        f: SearchPredicateFactory,
        root: SimpleBooleanPredicateClausesCollector<out SimpleBooleanPredicateClausesCollector<*>>
    ) {
        root.add(f.bool().with {
            it.must(
                f.range().field(filter.field).between(
                    filter.minValue, RangeBoundInclusion.INCLUDED,
                    filter.maxValue, RangeBoundInclusion.INCLUDED,
                )
            )
        })
    }

    private fun applyValueFilter(
        filter: FilterParameter,
        f: SearchPredicateFactory,
        root: SimpleBooleanPredicateClausesCollector<out SimpleBooleanPredicateClausesCollector<*>>
    ) {
        root.add(f.bool().with {
            for (value in filter.values) {
                it.should(f.match().fields(filter.field).matching(value))
            }
        })
    }

    private fun getFilters(raw: Map<String, String>): List<FilterParameter> =
        raw.toMutableMap().entries.apply {
            removeIf { it.key.equals("size", ignoreCase = true) }
            removeIf { it.key.equals("page", ignoreCase = true) }
            removeIf { it.key.equals("sort", ignoreCase = true) }
            removeIf { it.key.equals("query", ignoreCase = true) }
        }.mapNotNull { entry ->
            val range = entry.value.split("-").mapNotNull { it.toDoubleOrNull() }
            val values = entry.value.split(",")

            if (range.size > 1) {
                FilterParameter(field = entry.key, minValue = range[0], maxValue = range[1])
            } else if (values.isNotEmpty()) {
                FilterParameter(field = entry.key, values = values)
            } else {
                null
            }
        }.toList()
}