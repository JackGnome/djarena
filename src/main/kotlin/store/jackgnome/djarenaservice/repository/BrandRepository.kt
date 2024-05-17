package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import store.jackgnome.djarenaservice.model.brand.BrandEntity

interface BrandRepository : CrudRepository<BrandEntity, UUID> {
    fun existsByName(name: String): Boolean
    fun findAll(pageable: Pageable): Page<BrandEntity>
}