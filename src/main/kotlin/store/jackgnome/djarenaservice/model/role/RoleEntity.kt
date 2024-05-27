package store.jackgnome.djarenaservice.model.role

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID
import org.hibernate.annotations.UuidGenerator

@Table(name = "roles")
@Entity
class RoleEntity(
    @Column(name = "name")
    var name: String
) {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "UUID")
    var id: UUID? = null

    override fun toString(): String = "${this::class.simpleName}(id=$id, name='$name')"
}