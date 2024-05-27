package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import store.jackgnome.djarenaservice.model.brand.BrandEntity

@Repository
interface BrandRepository : CrudRepository<BrandEntity, UUID> {
    fun existsByName(name: String): Boolean
    fun findAll(pageable: Pageable): Page<BrandEntity>
    fun findAllByArchived(isArchived: Boolean, pageable: Pageable): Page<BrandEntity>
}