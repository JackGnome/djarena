package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import store.jackgnome.djarenaservice.model.brand.BrandCreateRequest
import store.jackgnome.djarenaservice.model.brand.BrandDto
import store.jackgnome.djarenaservice.model.brand.BrandEntity

val brandMapper: BrandMapper = Mappers.getMapper(BrandMapper::class.java)

@Mapper
interface BrandMapper {
    fun toEntity(request: BrandCreateRequest): BrandEntity
    fun toDto(entity: BrandEntity): BrandDto
}