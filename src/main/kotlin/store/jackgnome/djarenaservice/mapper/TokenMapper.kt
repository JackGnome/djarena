package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import store.jackgnome.djarenaservice.model.token.AuthTokenDto
import store.jackgnome.djarenaservice.model.token.AuthTokenEntity

@Mapper(componentModel = ComponentModel.SPRING)
interface TokenMapper {

    fun toDto(entity: AuthTokenEntity): AuthTokenDto
}