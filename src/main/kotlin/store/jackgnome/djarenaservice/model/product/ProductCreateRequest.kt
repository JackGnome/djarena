package store.jackgnome.djarenaservice.model.product

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class ProductCreateRequest (
    @field:NotBlank(message = "Vendor code is required")
    @field:Max(10, message = "Character limit exceeded. Max length is 10")
    var vendorCode: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(max = 255, message = "Character limit exceeded. Max length is 255")
    var name: String,
    var price: Double?,
    var brandId: UUID?,
)
