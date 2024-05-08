package store.jackgnome.djarenaservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import store.jackgnome.djarenaservice.mapper.ProductMapper
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.repository.ProductRepository

@Service
class ProductService {

    @Autowired
    private lateinit var repository: ProductRepository;

    private var mapper: ProductMapper = ProductMapper.INSTANCE;

    fun getProducts(pageable: Pageable): Page<ProductDto> {

        return repository.findAll(pageable).map(mapper::toDto)
    }

}