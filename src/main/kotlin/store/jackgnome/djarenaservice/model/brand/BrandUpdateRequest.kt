package store.jackgnome.djarenaservice.model.brand

import java.util.UUID

data class BrandUpdateRequest (
    val id: UUID,
    val name: String,
    val brandId: UUID,
)