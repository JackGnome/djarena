package store.jackgnome.djarenaservice.model.token

import java.time.Instant

data class AuthTokenDto(
    var value: String,
    var type: TokenType,
    var expiredAt: Instant,
    var revoked: Boolean,
)