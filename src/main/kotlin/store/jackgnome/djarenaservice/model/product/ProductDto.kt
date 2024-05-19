package store.jackgnome.djarenaservice.model.product

import java.util.UUID
import store.jackgnome.djarenaservice.model.brand.BrandDto

data class ProductDto (
    var id: UUID,
    var vendorCode: String,
    var name: String,
    var price: Double?,
    var preview: String,
    var brand: BrandDto?,
    var isArchived: Boolean,
)