package store.jackgnome.djarenaservice.model.brand

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField
import store.jackgnome.djarenaservice.model.base.BaseAuditEntity
import store.jackgnome.djarenaservice.model.product.ProductEntity

@Entity
@Table(name = "brand")
class BrandEntity(
    @KeywordField
    @Column(name = "name", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    var name: String,

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    var products: List<ProductEntity>

) : BaseAuditEntity() {
    override fun toString(): String = "${this::class.simpleName}(id=$id, name='$name')"
}