package store.jackgnome.djarenaservice.exception.wrapper

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import store.jackgnome.djarenaservice.model.exception.ApiException

class ConstraintViolationExceptionWrapper (val exception: ConstraintViolationException) :
    ApiException(exception.message ?: "") {

    private val violations = exception.constraintViolations
        .map { Violation(it.propertyPath.toString(), it.message) }
        .toList()

    override val payload: Any = Payload(violations)
    override val statusCode: HttpStatus = HttpStatus.BAD_REQUEST

    data class Payload(val violations: List<Violation>)
    data class Violation(val fieldName: String, val message: String?)
}