package store.jackgnome.djarenaservice.model.brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID
import org.hibernate.annotations.UuidGenerator
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField
import store.jackgnome.djarenaservice.model.product.ProductEntity

@Entity
@Table(name = "brand")
class BrandEntity(

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "UUID")
    var id: UUID?,

    @KeywordField
    @Column(name = "name", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    var name: String,

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    var products: List<ProductEntity>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        val that = other as BrandEntity?
        return id == that!!.id
    }

    override fun hashCode(): Int = 23

    override fun toString(): String = "${this::class.simpleName}(id=$id, name='$name')"
}