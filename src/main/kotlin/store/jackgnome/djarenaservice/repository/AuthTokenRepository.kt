package store.jackgnome.djarenaservice.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import store.jackgnome.djarenaservice.model.token.AuthTokenEntity
import store.jackgnome.djarenaservice.model.user.UserEntity

interface AuthTokenRepository : CrudRepository<AuthTokenEntity, UUID> {
    fun findAllByUserAndDeviceId(user: UserEntity, deviceId: String): List<AuthTokenEntity>
    fun findByValue(value: String): AuthTokenEntity?
}