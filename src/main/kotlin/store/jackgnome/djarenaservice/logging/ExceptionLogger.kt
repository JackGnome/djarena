package store.jackgnome.djarenaservice.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ExceptionLogger(
    @Value("\${logging.exception-handler-trace:false}") val traceEnabled: Boolean
) {

    @Value("\${logging.service-code}")
    private lateinit var serviceCode: String

    private val logger = KotlinLogging.logger {}

    fun print(exception: Exception, status: HttpStatus) {
        val message = StringBuilder()
        message.append('[').append(serviceCode).append(']')
            .append(" Caught ").append(exception.javaClass.simpleName)
            .append(" with message: ").append(exception.message)
            .append("; Thrown in ").append(exception.stackTrace.first().toString())

        if (traceEnabled) {
            message.append("\nException stack trace:\n")
                .append(exception.stackTraceToString())
        }

        if (status.is4xxClientError) {
            logger.warn { message.toString() }
        } else if (status.is5xxServerError) {
            logger.error { message.toString() }
        } else {
            logger.debug { message.toString() }
        }
    }
}