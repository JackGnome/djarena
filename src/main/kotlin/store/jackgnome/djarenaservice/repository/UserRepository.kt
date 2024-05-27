package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import store.jackgnome.djarenaservice.model.user.UserEntity

@Repository
interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}