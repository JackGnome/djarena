package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import store.jackgnome.djarenaservice.model.role.RoleEntity

@Repository
interface RoleRepository : CrudRepository<RoleEntity, UUID> {
    fun findByName(name: String): RoleEntity?
}