package store.jackgnome.djarenaservice.model.product

import java.util.UUID
import store.jackgnome.djarenaservice.model.brand.BrandDto

data class ProductSearchDto (
    var id: UUID,
    var name: String,
    var price: Double?,
    var preview: String,
)