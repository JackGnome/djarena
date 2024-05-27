package store.jackgnome.djarenaservice.model.auth

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceId: String,
)