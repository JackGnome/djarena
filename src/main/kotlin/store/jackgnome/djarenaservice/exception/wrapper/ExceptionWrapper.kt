package store.jackgnome.djarenaservice.exception.wrapper

import org.springframework.http.HttpStatus
import store.jackgnome.djarenaservice.model.exception.ApiException

class ExceptionWrapper(val exception: Exception) : ApiException(exception.message.toString()) {
    override val payload: Any = ""
    override val statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    override val code = exception::class.simpleName.toString()
}