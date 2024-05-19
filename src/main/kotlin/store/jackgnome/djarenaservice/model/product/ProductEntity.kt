package store.jackgnome.djarenaservice.model.product


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue
import store.jackgnome.djarenaservice.model.base.BaseAuditEntity
import store.jackgnome.djarenaservice.model.brand.BrandEntity


@Entity
@Table(name = "product")
@Indexed
class ProductEntity(
    @Column(name = "vendor_code", nullable = false, unique = true, columnDefinition = "varchar(10)")
    var vendorCode: String,

    @Column(name = "name", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    @FullTextField
    var name: String,

    @Column(name = "price", updatable = true, nullable = true, columnDefinition = "NUMERIC(10, 4)")
    @GenericField(sortable = Sortable.YES)
    var price: Double?,

    @Column(name = "preview", updatable = true, nullable = false, columnDefinition = "CHARACTER(1024)")
    var preview: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @IndexedEmbedded
    @AssociationInverseSide(inversePath = ObjectPath(PropertyValue(propertyName = "products")))
    var brand: BrandEntity? = null

) : BaseAuditEntity() {
    override fun toString(): String = "${this::class.simpleName}(id=$id, name=$name, price=$price, preview=$preview)"
}