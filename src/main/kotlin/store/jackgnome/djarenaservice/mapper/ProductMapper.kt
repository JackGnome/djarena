package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductEntity

@Mapper()
abstract class ProductMapper {
    companion object {
        val INSTANCE: ProductMapper = Mappers.getMapper(ProductMapper::class.java)
    }


    abstract fun toDto(entity: ProductEntity): ProductDto
}