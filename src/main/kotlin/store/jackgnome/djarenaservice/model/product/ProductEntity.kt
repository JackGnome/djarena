package store.jackgnome.djarenaservice.model.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
class ProductEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHARACTER(10)")
    val id: String = "";

    @Column(name = "name", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    val name: String = "";

    @Column(name = "price", updatable = true, nullable = true, columnDefinition = "INT")
    val price: Double? = null;

    @Column(name = "preview", updatable = true, nullable = false, columnDefinition = "CHARACTER(1024)")
    val preview: String = "";
}