package store.jackgnome.djarenaservice.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import store.jackgnome.djarenaservice.model.product.ProductCreateRequest
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductSearchDto
import store.jackgnome.djarenaservice.model.product.ProductUpdateRequest
import store.jackgnome.djarenaservice.service.ProductSearchService
import store.jackgnome.djarenaservice.service.ProductService

@RestController
@RequestMapping("/api/v1/products")
@Validated
class ProductController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productSearchService: ProductSearchService

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(@RequestParam("isArchived") isArchived: Boolean?, pageable: Pageable): Page<ProductDto> {
        return productService.getAll(isArchived, pageable)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable id: UUID): ProductDto {
        return productService.getById(id)
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun search(
        @RequestParam("query", defaultValue = "") query: String,
        pageable: Pageable,
        @RequestParam filters: Map<String, String>
    ): Page<ProductSearchDto> {
        return productSearchService.search(query, pageable, filters)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody product: ProductCreateRequest): ProductDto {
        return productService.create(product)
    }

    @PutMapping("/{id}/preview")
    @ResponseStatus(HttpStatus.OK)
    fun updatePreview(@PathVariable id: UUID, @RequestParam("file") file: MultipartFile): ProductDto {
        return productService.updatePreview(file, id)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun update(@Valid @RequestBody request: ProductUpdateRequest): ProductDto {
        return productService.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun archive(@PathVariable id: UUID): ProductDto {
        return productService.archive(id)
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun restore(@PathVariable id: UUID): ProductDto {
        return productService.restore(id)
    }
}