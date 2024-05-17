package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import store.jackgnome.djarenaservice.model.product.ProductCreateRequest
import store.jackgnome.djarenaservice.model.product.ProductDto
import store.jackgnome.djarenaservice.model.product.ProductEntity
import store.jackgnome.djarenaservice.model.product.ProductSearchDto

val productMapper: ProductMapper = Mappers.getMapper(ProductMapper::class.java)

@Mapper
interface ProductMapper {
    fun toDto(entity: ProductEntity): ProductDto

    fun toSearchDto(dto: ProductEntity): ProductSearchDto

    @Mapping(target = "preview", constant = "")
    fun toEntity(request: ProductCreateRequest): ProductEntity
}