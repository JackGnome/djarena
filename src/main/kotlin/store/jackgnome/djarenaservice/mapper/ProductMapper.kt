package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import store.jackgnome.djarenaservice.model.product.ProductCreateRequest
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductEntity
import store.jackgnome.djarenaservice.model.product.ProductUpdateRequest

@Mapper
abstract class ProductMapper {
    companion object {
        val INSTANCE: ProductMapper = Mappers.getMapper(ProductMapper::class.java)
    }

    abstract fun toDto(entity: ProductEntity): ProductDto
    abstract fun toEntity(dto: ProductDto): ProductEntity

    @Mapping(target = "preview", ignore = true)
    abstract fun toEntity(request: ProductCreateRequest): ProductEntity

    @Mapping(target = "preview", ignore = true)
    abstract fun toEntity(request: ProductUpdateRequest): ProductEntity

    abstract fun toDto(request: ProductCreateRequest): ProductDto
}