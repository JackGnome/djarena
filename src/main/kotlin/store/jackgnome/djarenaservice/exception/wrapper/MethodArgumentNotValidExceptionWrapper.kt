package store.jackgnome.djarenaservice.exception.wrapper

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import store.jackgnome.djarenaservice.model.exception.ApiException

class MethodArgumentNotValidExceptionWrapper(val exception: MethodArgumentNotValidException) :
    ApiException(exception.message) {

    private val violations = exception.bindingResult.fieldErrors
        .map { Violation(it.field, it.defaultMessage) }
        .toList()

    override val payload: Any = Payload(violations)
    override val statusCode: HttpStatus = HttpStatus.BAD_REQUEST

    data class Payload(val violations: List<Violation>)
    data class Violation(val fieldName: String, val message: String?)
}