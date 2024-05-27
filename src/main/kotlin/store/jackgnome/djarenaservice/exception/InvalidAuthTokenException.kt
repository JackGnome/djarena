package store.jackgnome.djarenaservice.exception

import org.springframework.http.HttpStatus
import store.jackgnome.djarenaservice.model.exception.ApiException

class InvalidAuthTokenException(tokenValue: String? = null) : ApiException("Invalid auth token $tokenValue") {

    override val payload: Any = Payload(tokenValue)
    override val statusCode: HttpStatus = HttpStatus.FORBIDDEN

    data class Payload(val tokenValue: String?)
}