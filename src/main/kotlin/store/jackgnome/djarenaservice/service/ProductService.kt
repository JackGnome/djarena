package store.jackgnome.djarenaservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import store.jackgnome.djarenaservice.exception.ItemAlreadyExistsException
import store.jackgnome.djarenaservice.exception.ItemNotFoundException
import store.jackgnome.djarenaservice.mapper.productMapper
import store.jackgnome.djarenaservice.model.product.ProductCreateRequest
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductEntity
import store.jackgnome.djarenaservice.model.product.ProductUpdateRequest
import store.jackgnome.djarenaservice.repository.ProductRepository
import store.jackgnome.djarenaservice.storage.ProductPreviewStorage

@Service
class ProductService {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var repository: ProductRepository

    @Autowired
    private lateinit var previewStorage: ProductPreviewStorage

    @Autowired
    private lateinit var brandService: BrandService

    val logger = KotlinLogging.logger {}

    fun getAll(pageable: Pageable): Page<ProductDto> {
        return repository.findAll(pageable).map(productMapper::toDto)
    }

    fun getById(id: UUID): ProductDto {
        return productMapper.toDto(getProductEntity(id))
    }

    @Transactional
    fun create(request: ProductCreateRequest): ProductDto {
        assertProductEntityNotExistsByVendorCode(request.vendorCode)

        val productEntity = productMapper.toEntity(request)
        productEntity.brand = request.brandId?.let(brandService::getBrandEntity)
        productEntity.preview = previewStorage.getDefaultImageUrl()
        return productMapper.toDto(productRepository.save(productEntity))
    }

    @Transactional
    fun update(request: ProductUpdateRequest): ProductDto {
        val productEntity = getProductEntity(request.id)

        assertProductEntityNotExistsByVendorCodeAndIdNot(request.vendorCode, request.id)

        productEntity.name = request.name
        productEntity.price = request.price
        productEntity.vendorCode = request.vendorCode
        productEntity.brand = request.brandId?.let(brandService::getBrandEntity)

        return productMapper.toDto(productRepository.save(productEntity))
    }

    @Transactional
    fun updatePreview(preview: MultipartFile, id: UUID): ProductDto {
        val productEntity = getProductEntity(id)

        productEntity.preview = previewStorage.save(preview, id)

        return productMapper.toDto(productRepository.save(productEntity))
    }

    private fun assertProductEntityNotExistsByVendorCode(vendorCode: String) {
        if (productRepository.existsByVendorCode(vendorCode)) {
            throw ItemAlreadyExistsException(ProductEntity::class.simpleName.toString(), "vendorCode", vendorCode)
        }
    }

    private fun assertProductEntityNotExistsByVendorCodeAndIdNot(vendorCode: String, id: UUID) {
        if (productRepository.existsByVendorCodeAndIdNot(vendorCode, id)) {
            throw ItemAlreadyExistsException(ProductEntity::class.simpleName.toString(), "vendorCode", vendorCode)
        }
    }

    private fun getProductEntity(id: UUID): ProductEntity = productRepository.findById(id)
        .orElseThrow { ItemNotFoundException(ProductEntity::class.simpleName.toString(), "id", id.toString()) }
}