package com.practice.kopring.oauth.application

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.domain.RefreshToken
import com.practice.kopring.auth.domain.dto.JwtDto
import com.practice.kopring.exception.NotExistsUserException
import com.practice.kopring.exception.TokenExpiredException
import com.practice.kopring.exception.TokenInvalidException
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.infrastructure.UserRepository
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val userRedisCacheService: UserRedisCacheService
) {
    fun refresh(refreshToken: String?): JwtDto {
        val userId: String? = this.jwtTokenProvider.getAccountName(refreshToken)
        if (userId.isNullOrBlank()) throw NotExistsUserException()

        val user: UserEntity = this.userRepository.findByIdOrNull(UUID.fromString(userId))
            ?: throw NotExistsUserException()
        val refreshTokenFromRedis: String? = this.userRedisCacheService.getWithUserId(userId)

        if (refreshToken.isNullOrBlank()) throw TokenExpiredException()
        if (!this.jwtTokenProvider.validate(refreshTokenFromRedis)) throw TokenExpiredException()
        else if (!refreshTokenFromRedis.equals(refreshToken)) throw TokenInvalidException()

        val newAccessToken: String = this.jwtTokenProvider.createAccessToken(user.id.toString(), user.role)
        val newRefreshToken: String = this.jwtTokenProvider.createRefreshToken(user.id.toString())

        this.userRedisCacheService.save(
            RefreshToken(newRefreshToken, user.id.toString()),
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
