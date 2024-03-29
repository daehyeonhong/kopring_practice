package com.practice.kopring.auth.application

import com.practice.kopring.auth.dto.JwtDto
import com.practice.kopring.auth.dto.JwtTokenResponse
import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.common.exception.auth.TokenExpiredException
import com.practice.kopring.common.exception.auth.TokenInvalidException
import com.practice.kopring.common.exception.user.NotExistsUserException
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val userRedisCacheService: UserRedisCacheService,
) {
    fun refresh(refreshToken: String?): JwtTokenResponse {
        val userId: String = this.jwtTokenProvider.getSubject(refreshToken)

        val user: UserEntity = this.userRepository.findByIdOrNull(UUID.fromString(userId))
            ?: throw NotExistsUserException()
        val refreshTokenFromRedis: String? = this.userRedisCacheService.getWithUserId(userId)

        if (refreshToken.isNullOrBlank()) throw TokenExpiredException()
        if (!this.jwtTokenProvider.validate(refreshTokenFromRedis)) throw TokenExpiredException()
        else if (!refreshTokenFromRedis.equals(refreshToken)) throw TokenInvalidException()

        val newAccessToken: String = this.jwtTokenProvider.createAccessToken(user.id.toString(), user.role)
        val newRefreshToken: String = this.jwtTokenProvider.createRefreshToken(user.id.toString())

        this.userRedisCacheService.saveRefreshToken(
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
        val access: String? = this.resolveToken(accessToken)
        if (!jwtTokenProvider.validate(access)) throw TokenInvalidException()
        val userId: String = this.jwtTokenProvider.getSubject(access)
        if (!this.userRepository.existsById(UUID.fromString(userId))) throw NotExistsUserException()
        this.userRedisCacheService.getWithUserId(userId)?.let { this.userRedisCacheService.delete(it) }
    }

    private fun resolveToken(bearerToken: String?): String? {
        return when {
            !bearerToken.isNullOrBlank() && bearerToken.startsWith(Token.BEARER_PREFIX.value) ->
                bearerToken.replace(Token.BEARER_PREFIX.value, "")

            else -> null
        }
    }

    fun findByOneTimeToken(oneTimeToken: String): String? =
        this.userRedisCacheService.getUserIdWithOneTimeTokenToken(oneTimeToken)

    fun revokeOneTimeToken(oneTimeToken: String) {
        this.userRedisCacheService.deleteOneTimeToken(oneTimeToken)
    }

    fun createTokenWithUserId(userId: String): JwtDto {
        val accessToken = this.jwtTokenProvider.createAccessToken(userId, Role.USER)
        val refreshToken = this.jwtTokenProvider.createRefreshToken(userId)
        return JwtDto(
            Role.USER.key,
            accessToken,
            refreshToken,
            this.jwtTokenProvider.refreshTokenExpireTime()
        )
    }
}
