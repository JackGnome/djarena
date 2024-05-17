package store.jackgnome.djarenaservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import java.util.UUID
import org.hibernate.search.mapper.orm.Search
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import store.jackgnome.djarenaservice.mapper.productMapper
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