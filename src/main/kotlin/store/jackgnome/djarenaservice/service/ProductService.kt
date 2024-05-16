package store.jackgnome.djarenaservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import java.util.UUID
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
import org.springframework.web.multipart.MultipartFile
import store.jackgnome.djarenaservice.mapper.productMapper
import store.jackgnome.djarenaservice.model.product.FilterParameter
import store.jackgnome.djarenaservice.model.product.ProductCreateRequest
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductEntity
import store.jackgnome.djarenaservice.model.product.ProductUpdateRequest
import store.jackgnome.djarenaservice.repository.ProductRepository
import store.jackgnome.djarenaservice.storage.ProductPreviewStorage

@Service
class ProductService : ApplicationListener<ApplicationReadyEvent> {

    val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var repository: ProductRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var previewStorage: ProductPreviewStorage

//    private var mapper: ProductMapper = productMapper

    fun get(pageable: Pageable): Page<ProductDto> {
        return repository.findAll(pageable).map(productMapper::toDto)
    }

    @Transactional
    fun create(request: ProductCreateRequest): ProductDto {
        val productEntity = productMapper.toEntity(request)
        productEntity.preview = previewStorage.getDefaultImageUrl()
        entityManager.persist(productEntity)
        return productMapper.toDto(productEntity)
    }

    @Transactional
    fun update(request: ProductUpdateRequest): ProductDto {
        val storedProduct = repository.findById(request.id).get()

        storedProduct.name = request.name
        storedProduct.price = request.price
        storedProduct.vendorCode = request.vendorCode

        entityManager.persist(storedProduct)
        return productMapper.toDto(storedProduct)
    }

    fun search(searchQuery: String, pageable: Pageable, filters: Map<String, String>): Page<ProductDto> {
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

                if (filterParams.isNotEmpty()) {
                    for (filter in filterParams) {
                        when (filter.type) {
                            FilterParameter.FilterType.RangeFilter -> {
                                root.add(
                                    f.bool().with { b ->
                                        b.must(
                                            f.range().field(filter.field)
                                                .between(
                                                    filter.minValue, RangeBoundInclusion.INCLUDED,
                                                    filter.maxValue, RangeBoundInclusion.INCLUDED,
                                                )
                                        )
                                    }
                                )
                            }

                            FilterParameter.FilterType.ValueFilter -> {
                                for (value in filter.values) {
                                    root.add(f.bool().with { b ->
                                        b.should(f.match().fields(filter.field).matching(value))
                                    })
                                }
                            }

                            FilterParameter.FilterType.UnknownFilter -> {}
                        }
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

        val products: List<ProductDto> = result.hits()
            .map { r -> r as ProductEntity }
            .map { r -> productMapper.toDto(r) }
            .toList()

        return PageImpl(products, pageable, result.total().hitCount())
    }

    fun getFilters(raw: Map<String, String>): List<FilterParameter> {
        val r = raw.toMutableMap()
        r.entries.removeIf { it.key.equals("size", ignoreCase = true) }
        r.entries.removeIf { it.key.equals("page", ignoreCase = true) }
        r.entries.removeIf { it.key.equals("sort", ignoreCase = true) }
        r.entries.removeIf { it.key.equals("query", ignoreCase = true) }
        val filters = mutableListOf<FilterParameter>()
        r.entries.forEach { entry ->
            val range = entry.value.split("-").mapNotNull { it.toDoubleOrNull() }
            val values = entry.value.split(",")
            if (range.size > 1) {
                val filterParam = FilterParameter(field = entry.key, minValue = range[0], maxValue = range[1])
                filters.add(filterParam)
            } else if (values.isNotEmpty()) {
                val filterParam = FilterParameter(field = entry.key, values = values)
                filters.add(filterParam)
            }
        }

        return filters
    }

    @Transactional
    fun updatePreview(preview: MultipartFile, id: UUID): ProductDto {
        val previewUrl = previewStorage.save(preview, id)
        val productEntity = productRepository.findById(id).get()
        productEntity.preview = previewUrl
        entityManager.persist(productEntity)
        return productMapper.toDto(productEntity)
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
}