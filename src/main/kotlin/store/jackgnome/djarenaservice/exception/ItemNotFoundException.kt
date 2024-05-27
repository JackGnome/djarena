package store.jackgnome.djarenaservice.exception

import kotlin.reflect.KClass
import org.springframework.http.HttpStatus
import store.jackgnome.djarenaservice.model.exception.ApiException

class ItemNotFoundException(
    itemName: String,
    fieldName: String,
    fieldValue: String
) :
    ApiException("$itemName with $fieldName '$fieldValue' is not found") {

    constructor(itemClass: KClass<*>, fieldName: String, fieldValue: String) : this(
        itemClass.simpleName.toString(),
        fieldName,
        fieldValue
    )

    override val payload: Any = Payload(itemName, fieldName, fieldValue)
    override val statusCode: HttpStatus = HttpStatus.NOT_FOUND

    data class Payload(val itemName: String, val fieldName: String, val fieldValue: String)
}