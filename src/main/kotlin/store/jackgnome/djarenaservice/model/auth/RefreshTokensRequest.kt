package store.jackgnome.djarenaservice.model.auth

data class RefreshTokensRequest (
    val refreshToken: String,
    val deviceId: String,
)