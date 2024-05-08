package store.jackgnome.djarenaservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.service.ProductService

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping
    fun getProducts(pageable: Pageable): Page<ProductDto>{
        return productService.getProducts(pageable)
    }
}