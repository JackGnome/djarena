package store.jackgnome.djarenaservice.model.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseAuditEntity : BaseEntity() {

    @CreatedBy
    @Column(name = "created_by")
    var createdBy: UUID? = null

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: Instant? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: UUID? = null

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
}