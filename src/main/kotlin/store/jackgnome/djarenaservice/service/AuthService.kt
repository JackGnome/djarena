package store.jackgnome.djarenaservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import store.jackgnome.djarenaservice.exception.InvalidAuthTokenException
import store.jackgnome.djarenaservice.exception.ItemAlreadyExistsException
import store.jackgnome.djarenaservice.exception.ItemNotFoundException
import store.jackgnome.djarenaservice.mapper.UserMapper
import store.jackgnome.djarenaservice.model.auth.LoginRequest
import store.jackgnome.djarenaservice.model.auth.LoginResponse
import store.jackgnome.djarenaservice.model.auth.RefreshTokensRequest
import store.jackgnome.djarenaservice.model.auth.RefreshTokensResponse
import store.jackgnome.djarenaservice.model.auth.SignUpRequest
import store.jackgnome.djarenaservice.model.auth.SignUpResponse
import store.jackgnome.djarenaservice.model.user.UserEntity
import store.jackgnome.djarenaservice.repository.UserRepository

@Service
class AuthService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var roleService: RoleService

    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw ItemAlreadyExistsException(UserEntity::class, "email", request.email)
        }

        val userEntity = userMapper.toEntity(request).apply {
            password = passwordEncoder.encode(request.password)
            roles = setOf(roleService.getDefaultRoleEntity())
        }

        val user = userRepository.save(userEntity)
        val accessToken = tokenService.generateAccessToken(user, request.deviceId)
        val refreshToken = tokenService.generateRefreshToken(user, request.deviceId)
        return SignUpResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
        val user = getUser(request.email)
        tokenService.revokeTokens(user, request.deviceId)
        val accessToken = tokenService.generateAccessToken(user, request.deviceId)
        val refreshToken = tokenService.generateRefreshToken(user, request.deviceId)
        return LoginResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    @Transactional
    fun refreshTokens(request: RefreshTokensRequest): RefreshTokensResponse {
        val userEmail: String =
            tokenService.extractUsername(request.refreshToken) ?: throw InvalidAuthTokenException(request.refreshToken)
        val user = userRepository.findByEmail(userEmail) ?: throw InvalidAuthTokenException(request.refreshToken)

        if (!tokenService.isRefreshTokenValid(request.refreshToken, user, request.deviceId)) {
            throw InvalidAuthTokenException(request.refreshToken)
        }

        tokenService.revokeTokens(user, request.deviceId)
        val accessToken = tokenService.generateAccessToken(user, request.deviceId)
        val refreshToken = tokenService.generateRefreshToken(user, request.deviceId)
        return RefreshTokensResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    private fun getUser(email: String): UserEntity =
        userRepository.findByEmail(email) ?: throw ItemNotFoundException(
            UserEntity::class.simpleName.toString(),
            "email",
            email
        )
}