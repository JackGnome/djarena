package store.jackgnome.djarenaservice.utils

import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import store.jackgnome.djarenaservice.model.user.UserEntity


fun getCurrentUserId(): UUID? {
    val authentication = SecurityContextHolder.getContext().authentication
    if (authentication == null || !authentication.isAuthenticated) {
        return null
    }

    return (authentication.principal as UserEntity).id
}