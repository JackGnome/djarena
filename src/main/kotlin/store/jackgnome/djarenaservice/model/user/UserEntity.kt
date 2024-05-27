package store.jackgnome.djarenaservice.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import store.jackgnome.djarenaservice.model.base.BaseAuditEntity
import store.jackgnome.djarenaservice.model.role.RoleEntity

@Table(name = "users")
@Entity
class UserEntity(
    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    private var password: String
) : BaseAuditEntity(), UserDetails {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<RoleEntity> = setOf()

    fun setPassword(password: String) {
        this.password = password
    }

    override fun toString(): String = "${this::class.simpleName}(id=$id, email='$email')"

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }.toMutableSet()

    override fun getPassword(): String = password
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = isNotArchived
    override fun isAccountNonLocked(): Boolean = isNotArchived
    override fun isCredentialsNonExpired(): Boolean = isNotArchived
    override fun isEnabled(): Boolean = isNotArchived
}