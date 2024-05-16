package store.jackgnome.djarenaservice.model.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.bridge.builtin.impl.DefaultDoubleBridge
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField
import store.jackgnome.djarenaservice.bridge.DoubleBridge


@Entity
@Table(name = "product")
@Indexed
data class ProductEntity(

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHARACTER(10)")
    var id: String = "",

    @Column(name = "name", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    @FullTextField
    var name: String = "",

    @Column(name = "price", updatable = true, nullable = true, columnDefinition = "INT")
    @KeywordField(
        sortable = Sortable.YES,
        valueBridge = ValueBridgeRef(type = DoubleBridge::class),
    )
    var price: Double,

    @Column(name = "preview", updatable = true, nullable = false, columnDefinition = "CHARACTER(1024)")
    var preview: String = "",
)