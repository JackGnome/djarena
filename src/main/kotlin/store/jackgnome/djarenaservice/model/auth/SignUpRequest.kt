package store.jackgnome.djarenaservice.model.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val deviceId: String,
)
