package store.jackgnome.djarenaservice.exception.handler

import jakarta.validation.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import store.jackgnome.djarenaservice.exception.wrapper.ConstraintViolationExceptionWrapper
import store.jackgnome.djarenaservice.exception.wrapper.MethodArgumentNotValidExceptionWrapper
import store.jackgnome.djarenaservice.logging.ExceptionLogger
import store.jackgnome.djarenaservice.mapper.ExceptionMapper
import store.jackgnome.djarenaservice.model.exception.ApiException
import store.jackgnome.djarenaservice.model.exception.ApiExceptionDto

@ControllerAdvice
class GeneralExceptionHandler : DefaultHandlerExceptionResolver() {

    @Autowired
    private lateinit var exceptionLogger: ExceptionLogger

    @Autowired
    private lateinit var exceptionMapper: ExceptionMapper

    @ExceptionHandler(ApiException::class)
    fun handle(exception: ApiException): ResponseEntity<ApiExceptionDto> {
        exceptionLogger.print(exception, exception.statusCode)
        return ResponseEntity<ApiExceptionDto>(exceptionMapper.toDto(exception), exception.statusCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<ApiExceptionDto> {
        val exception = MethodArgumentNotValidExceptionWrapper(e)
        exceptionLogger.print(exception, exception.statusCode)
        return ResponseEntity<ApiExceptionDto>(exceptionMapper.toDto(exception), exception.statusCode)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(e: ConstraintViolationException): ResponseEntity<ApiExceptionDto> {
        val exception = ConstraintViolationExceptionWrapper(e)
        exceptionLogger.print(exception, exception.statusCode)
        return ResponseEntity<ApiExceptionDto>(exceptionMapper.toDto(exception), exception.statusCode)
    }
}