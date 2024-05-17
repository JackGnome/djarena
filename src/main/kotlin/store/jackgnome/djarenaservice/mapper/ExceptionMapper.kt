package store.jackgnome.djarenaservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel
import org.springframework.beans.factory.annotation.Value
import store.jackgnome.djarenaservice.model.exception.ApiException
import store.jackgnome.djarenaservice.model.exception.ApiExceptionDto

@Mapper(componentModel = ComponentModel.SPRING)
abstract class ExceptionMapper {

    @Value("\${logging.service-code}")
    private lateinit var serviceCode: String


    @Mapping(target="code", expression = "java(exception.getClass().getSimpleName())")
    abstract fun toDto(exception: ApiException, service: String = serviceCode): ApiExceptionDto
}