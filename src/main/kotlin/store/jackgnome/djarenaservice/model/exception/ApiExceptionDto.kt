package store.jackgnome.djarenaservice.model.exception

data class ApiExceptionDto (
    val code: String,
    val service: String,
    val message: String,
    val payload: Any,
)