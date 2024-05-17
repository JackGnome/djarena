package store.jackgnome.djarenaservice.model.exception

import org.springframework.http.HttpStatus

abstract class ApiException(message: String) : RuntimeException(message) {
    abstract val payload: Any
    abstract val statusCode: HttpStatus
}