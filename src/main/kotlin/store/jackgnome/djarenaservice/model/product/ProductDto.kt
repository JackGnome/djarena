package store.jackgnome.djarenaservice.model.product

data class ProductDto (
    var id: String,
    var name: String,
    var price: Double?,
    var preview: String,
)