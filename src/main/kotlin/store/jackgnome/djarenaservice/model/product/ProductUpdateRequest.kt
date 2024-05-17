package store.jackgnome.djarenaservice.model.product

import java.util.UUID

data class ProductUpdateRequest (
    var id: UUID,
    var vendorCode: String,
    var name: String,
    var price: Double?,
    var brandId: UUID?,
)