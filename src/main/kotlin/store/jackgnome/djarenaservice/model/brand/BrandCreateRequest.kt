package store.jackgnome.djarenaservice.model.brand

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BrandCreateRequest (
    @field:NotBlank(message = "Name is required")
    @field:Size(max = 255, message = "Character limit exceeded. Max length is 255")
    var name: String,
)