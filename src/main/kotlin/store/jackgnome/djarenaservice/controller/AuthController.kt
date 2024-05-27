package store.jackgnome.djarenaservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import store.jackgnome.djarenaservice.model.auth.LoginRequest
import store.jackgnome.djarenaservice.model.auth.LoginResponse
import store.jackgnome.djarenaservice.model.auth.RefreshTokensRequest
import store.jackgnome.djarenaservice.model.auth.RefreshTokensResponse
import store.jackgnome.djarenaservice.model.auth.SignUpRequest
import store.jackgnome.djarenaservice.model.auth.SignUpResponse
import store.jackgnome.djarenaservice.service.AuthService

@RestController
@RequestMapping("api/v1/auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody request: SignUpRequest): SignUpResponse {
        return authService.signUp(request)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return authService.login(request)
    }

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    fun refreshTokens(@RequestBody request: RefreshTokensRequest): RefreshTokensResponse {
        return authService.refreshTokens(request)
    }
}
