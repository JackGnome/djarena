package store.jackgnome.djarenaservice.service

import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import store.jackgnome.djarenaservice.exception.ItemAlreadyExistsException
import store.jackgnome.djarenaservice.exception.ItemNotFoundException
import store.jackgnome.djarenaservice.mapper.brandMapper
import store.jackgnome.djarenaservice.model.brand.BrandCreateRequest
import store.jackgnome.djarenaservice.model.brand.BrandDto
import store.jackgnome.djarenaservice.model.brand.BrandEntity
import store.jackgnome.djarenaservice.model.brand.BrandUpdateRequest
import store.jackgnome.djarenaservice.repository.BrandRepository

@Service
class BrandService {

    @Autowired
    private lateinit var repository: BrandRepository

    fun create(request: BrandCreateRequest): BrandDto {
        assertBrandEntityByNameNotExists(request.name)

        val brandEntity = brandMapper.toEntity(request)
        return brandMapper.toDto(repository.save(brandEntity))
    }

    fun get(id: UUID): BrandDto {
        return brandMapper.toDto(getBrandEntity(id))
    }

    fun getAll(pageable: Pageable): Page<BrandDto> {
        return repository.findAll(pageable).map(brandMapper::toDto)
    }

    @Transactional
    fun update(request: BrandUpdateRequest): BrandDto {
        val brandEntity = getBrandEntity(request.id)

        brandEntity.name = request.name
        return brandMapper.toDto(repository.save(brandEntity))
    }

    fun getBrandEntity(id: UUID): BrandEntity = repository.findById(id)
        .orElseThrow { ItemNotFoundException(BrandEntity::class.simpleName.toString(), "id", id.toString()) }

    fun assertBrandEntityExistsById(id: UUID) {

    }

    private fun assertBrandEntityByNameNotExists(name: String) {
        if (repository.existsByName(name)) {
            throw ItemAlreadyExistsException(BrandEntity::class.simpleName.toString(), "name", name)
        }
    }
}