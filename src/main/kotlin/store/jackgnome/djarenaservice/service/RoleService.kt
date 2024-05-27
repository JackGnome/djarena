package store.jackgnome.djarenaservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import store.jackgnome.djarenaservice.exception.ItemNotFoundException
import store.jackgnome.djarenaservice.model.role.RoleEntity
import store.jackgnome.djarenaservice.repository.RoleRepository

@Service
class RoleService {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Value("\${security.role.default}")
    private var defaultRole: String = "USER"

    fun getDefaultRoleEntity(): RoleEntity = getRoleEntityByName(defaultRole)

    fun getRoleEntityByName(name: String): RoleEntity =
        roleRepository.findByName(name) ?: throw ItemNotFoundException(RoleEntity::class, "name", name)
}