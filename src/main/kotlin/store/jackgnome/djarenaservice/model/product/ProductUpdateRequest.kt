package store.jackgnome.djarenaservice.model.product

data class ProductUpdateRequest (
    var id: String,
    var name: String,
    var price: Double,
)