package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import store.jackgnome.djarenaservice.model.auth.SignUpRequest
import store.jackgnome.djarenaservice.model.user.UserDto
import store.jackgnome.djarenaservice.model.user.UserEntity

@Mapper(componentModel = ComponentModel.SPRING)
interface UserMapper {
    fun toEntity(request: SignUpRequest): UserEntity
    fun toDto(entity: UserEntity): UserDto
}