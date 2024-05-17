package store.jackgnome.djarenaservice.exception

import org.springframework.http.HttpStatus
import store.jackgnome.djarenaservice.model.exception.ApiException

class ItemAlreadyExistsException(
    itemName: String,
    fieldName: String,
    fieldValue: String
) :
    ApiException("$itemName with $fieldName '$fieldValue' already exists") {


    override val payload: Any = Payload(itemName, fieldName, fieldValue)
    override val statusCode: HttpStatus = HttpStatus.CONFLICT

    data class Payload(val itemName: String, val fieldName: String, val fieldValue: String)
}