package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import store.jackgnome.djarenaservice.model.product.ProductEntity

@Repository
interface ProductRepository : CrudRepository<ProductEntity, UUID> {

    fun findAll(pageable: Pageable): Page<ProductEntity>
    fun existsByVendorCode(vendorCode: String): Boolean
    fun existsByVendorCodeAndIdNot(vendorCode: String, id: UUID): Boolean
    fun findAllByArchived(archived: Boolean, pageable: Pageable): Page<ProductEntity>
}