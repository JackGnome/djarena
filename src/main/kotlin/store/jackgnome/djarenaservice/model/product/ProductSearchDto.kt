package store.jackgnome.djarenaservice.model.product

import java.util.UUID

data class ProductSearchDto (
    var id: UUID,
    var vendorCode: String,
    var name: String,
    var price: Double?,
    var preview: String,
)