package store.jackgnome.djarenaservice.model.product

import java.util.UUID

data class ProductCreateRequest (
    var vendorCode: String,
    var name: String,
    var price: Double?,
    var brandId: UUID?,
)
