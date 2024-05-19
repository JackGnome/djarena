package store.jackgnome.djarenaservice.model.base

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Transient
import java.util.UUID
import org.hibernate.annotations.UuidGenerator
import store.jackgnome.djarenaservice.model.brand.BrandEntity

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "UUID")
    var id: UUID? = null

    @Column(name = "archived")
    protected var archived: Boolean = false

    @get:Transient
    val isArchived: Boolean
        get() = archived

    @get:Transient
    val isNotArchived: Boolean
        get() = !archived

    fun archive() {
        archived = true
    }

    fun restore() {
        archived = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        val that = other as BrandEntity?
        return id == that!!.id
    }

    override fun hashCode(): Int = 23

    override fun toString(): String = "${this::class.simpleName}(id=$id)"
}