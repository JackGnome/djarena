package store.jackgnome.djarenaservice.controller

import jakarta.validation.Valid
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import store.jackgnome.djarenaservice.model.brand.BrandCreateRequest
import store.jackgnome.djarenaservice.model.brand.BrandDto
import store.jackgnome.djarenaservice.model.brand.BrandUpdateRequest
import store.jackgnome.djarenaservice.service.BrandService

@RestController
@RequestMapping("/api/v1/brands")
class BrandController {

    @Autowired
    private lateinit var service: BrandService

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody request: BrandCreateRequest): BrandDto {
        return service.create(request)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: UUID): BrandDto {
        return service.get(id)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(@RequestParam("isArchived") isArchived: Boolean?, pageable: Pageable): Page<BrandDto> {
        return service.getAll(isArchived, pageable)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    fun update(@Valid @RequestBody request: BrandUpdateRequest): BrandDto {
        return service.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    fun archive(@PathVariable id: UUID): BrandDto {
        return service.archive(id)
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    fun restore(@PathVariable id: UUID): BrandDto {
        return service.restore(id)
    }
}