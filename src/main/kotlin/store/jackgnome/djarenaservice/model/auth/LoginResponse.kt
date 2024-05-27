package store.jackgnome.djarenaservice.model.auth

import store.jackgnome.djarenaservice.model.token.AuthTokenDto

data class LoginResponse(
    val accessToken: AuthTokenDto,
    val refreshToken: AuthTokenDto,
)