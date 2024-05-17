package store.jackgnome.djarenaservice.model.brand

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class BrandUpdateRequest (
    val id: UUID,

    @field:NotBlank(message = "Name is required")
    @field:Size(max = 255, message = "Character limit exceeded. Max length is 255")
    val name: String,
)