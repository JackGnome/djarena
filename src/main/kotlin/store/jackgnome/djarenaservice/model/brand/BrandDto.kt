package store.jackgnome.djarenaservice.model.brand

import java.util.UUID

data class BrandDto(
    var id: UUID,
    var name: String,
    var isArchived: Boolean,
)