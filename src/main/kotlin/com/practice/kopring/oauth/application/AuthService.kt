package com.practice.kopring.oauth.application

import com.auth0.jwt.exceptions.TokenExpiredException
import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.domain.dto.JwtDto
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.domain.enumerate.Role
import com.practice.kopring.user.domain.enumerate.UserRedisKey
import com.practice.kopring.user.infrastructure.UserRepository
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull

class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val userRedisCacheService: UserRedisCacheService
) {
    fun refresh(refreshToken: String?): JwtDto {
        val userId: String? = this.jwtTokenProvider.getAccountName(refreshToken)
        if (userId.isNullOrBlank()) throw java.lang.IllegalArgumentException()
        val user: UserEntity = this.userRepository.findByIdOrNull(UUID.fromString(userId))
            ?: throw IllegalArgumentException()
        val redisRT: String? = this.userRedisCacheService.getWithUserId(userId)

        if (refreshToken.isNullOrBlank()) {
            throw TokenExpiredException("", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        }
        if (!this.jwtTokenProvider.validate(redisRT)) {
            throw TokenExpiredException("", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        } else if (!redisRT.equals(refreshToken)) {
            throw TokenInvalidException()
        }
        val newAccessToken: String = this.jwtTokenProvider.createAccessToken(user.id.toString(), Role.USER)
        val newRefreshToken: String = this.jwtTokenProvider.createRefreshToken(user.id.toString())

        this.userRedisCacheService.update(
            UserRedisKey.USER_KEY.value + user.id,
            newRefreshToken,
            this.jwtTokenProvider.refreshTokenExpireTime()
        )
        return JwtDto(
            grantType = "Bearer",
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = this.jwtTokenProvider.getExpiration(newAccessToken)
        )
    }
}
