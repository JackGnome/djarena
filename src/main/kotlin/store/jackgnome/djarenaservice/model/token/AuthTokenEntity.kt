package store.jackgnome.djarenaservice.model.token

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import store.jackgnome.djarenaservice.model.user.UserEntity

@Table(name = "auth_tokens")
@Entity
class AuthTokenEntity(
    @Column(name = "value")
    var value: String,

    @Column(name = "type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    var type: TokenType,

    @Column(name = "created_at")
    var createdAt: Instant,

    @Column(name = "expired_at")
    var expiredAt: Instant,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @Column(name = "device_id")
    var deviceId: String,
) {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "UUID")
    var id: UUID? = null

    @Column(name = "revoked")
    var revoked: Boolean = false
}