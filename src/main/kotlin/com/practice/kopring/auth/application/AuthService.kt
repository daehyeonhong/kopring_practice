package com.practice.kopring.auth.application

import com.practice.kopring.auth.dto.JwtTokenResponse
import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.common.exception.auth.TokenExpiredException
import com.practice.kopring.common.exception.auth.TokenInvalidException
import com.practice.kopring.common.exception.user.NotExistsUserException
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.infrastructure.UserRepository
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val userRedisCacheService: UserRedisCacheService
) {
    fun refresh(refreshToken: String?): JwtTokenResponse {
        val userId: String? = this.jwtTokenProvider.getUserId(refreshToken)
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

        return JwtTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun revokeToken(accessToken: String) {
        val access: String? = this.jwtTokenProvider.resolveToken(accessToken)
        if (!jwtTokenProvider.validate(access)) throw TokenInvalidException()
        val userId: String = this.jwtTokenProvider.getUserId(access) ?: throw NotExistsUserException()
        if (!this.userRepository.existsById(UUID.fromString(userId))) throw NotExistsUserException()
        if (this.userRedisCacheService.getWithUserId(userId) != null) this.userRedisCacheService.delete(userId)
    }
}
