package store.jackgnome.djarenaservice.model.product

data class FilterParameter (
    var field: String = "",
    var minValue: Double = 0.0,
    var maxValue: Double = 0.0,
    var values: List<String> = emptyList(),
    var type: FilterType = FilterType.UnknownFilter,
) {

    constructor(field: String, minValue: Double, maxValue: Double) : this() {
        this.field = field
        this.minValue = minValue
        this.maxValue = maxValue
        type = FilterType.RangeFilter
    }

    constructor(field: String, values: List<String>): this() {
        this.field = field
        this.values = values
        type = FilterType.ValueFilter
    }

    enum class FilterType {
        RangeFilter,
        ValueFilter,
        UnknownFilter,
    }
}