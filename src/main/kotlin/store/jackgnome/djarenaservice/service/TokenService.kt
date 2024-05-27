package store.jackgnome.djarenaservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import store.jackgnome.djarenaservice.exception.InvalidAuthTokenException
import store.jackgnome.djarenaservice.mapper.TokenMapper
import store.jackgnome.djarenaservice.model.token.AuthTokenDto
import store.jackgnome.djarenaservice.model.token.AuthTokenEntity
import store.jackgnome.djarenaservice.model.token.TokenType
import store.jackgnome.djarenaservice.model.user.UserEntity
import store.jackgnome.djarenaservice.repository.AuthTokenRepository


@Service
class TokenService {
    @Value("\${security.jwt.secret-key}")
    private lateinit var jwtSecret: String

    @Value("\${security.access-token.expiration-time}")
    var accessTokenExpiration: Long = 3600

    @Value("\${security.refresh-token.expiration-time}")
    var refreshTokenExpiration: Long = 1728000

    @Autowired
    private lateinit var authTokenRepository: AuthTokenRepository

    @Autowired
    private lateinit var tokenMapper: TokenMapper

    fun <T> extractClaim(token: String?, claimsResolver: (c: Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    @Transactional
    fun revokeTokens(user: UserEntity, deviceId: String) {
        authTokenRepository.findAllByUserAndDeviceId(user, deviceId)
            .onEach { it.revoked = true }
            .forEach { authTokenRepository.save(it) }
    }

    fun extractUsername(token: String?): String? = extractClaim(token, Claims::getSubject)

    fun isAccessTokenValid(token: String?, userDetails: UserDetails): Boolean =
        isTokenValid(token, userDetails) && isCorrectTokenType(token, TokenType.ACCESS)

    fun isRefreshTokenValid(token: String?, userDetails: UserDetails, deviceId: String): Boolean =
        isTokenValid(token, userDetails)
                && isCorrectTokenType(token, TokenType.REFRESH)
                && isCorrectTokenDeviceId(token, deviceId)

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean =
        extractUsername(token) == userDetails.username && !isTokenExpired(token) && !isTokenRevoked(token)

    fun isCorrectTokenType(token: String?, tokenType: TokenType): Boolean =
        extractAllClaims(token)["token_type"] == tokenType.name

    fun isCorrectTokenDeviceId(token: String?, deviceId: String): Boolean =
        extractAllClaims(token)["device_id"] == deviceId

    fun generateAccessToken(user: UserEntity, deviceId: String): AuthTokenDto =
        generateToken(user, TokenType.ACCESS, accessTokenExpiration, deviceId)

    fun generateRefreshToken(user: UserEntity, deviceId: String): AuthTokenDto =
        generateToken(user, TokenType.REFRESH, refreshTokenExpiration, deviceId)

    private fun generateToken(user: UserEntity, type: TokenType, expiration: Long, deviceId: String): AuthTokenDto {
        val creationTime = Instant.now()
        val expirationTime = creationTime.plusSeconds(expiration)
        val jwt = buildToken(user, creationTime, expirationTime, type, deviceId)
        val token = AuthTokenEntity(jwt, type, creationTime, expirationTime, user, deviceId)
        return tokenMapper.toDto(authTokenRepository.save(token))
    }

    private fun buildToken(
        user: UserEntity,
        creationTime: Instant,
        expirationTime: Instant,
        type: TokenType,
        deviceId: String,
        extraClaims: Map<String, Any> = mutableMapOf()
    ): String {
        return Jwts.builder()
            .claims(extraClaims)
            .claim("token_type", type.name)
            .claim("device_id", deviceId)
            .subject(user.email)
            .issuedAt(Date.from(creationTime))
            .expiration(Date.from(expirationTime))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact()
    }

    private fun isTokenRevoked(token: String?): Boolean {
        if (token == null) throw InvalidAuthTokenException()
        return authTokenRepository.findByValue(token)?.revoked ?: throw InvalidAuthTokenException(token)
    }

    private fun isTokenExpired(token: String?): Boolean = extractExpirationTime(token).before(Date())

    private fun extractExpirationTime(token: String?): Date = extractClaim(token, Claims::getExpiration)

    private fun extractAllClaims(token: String?): Claims =
        Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}