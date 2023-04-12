package com.practice.kopring.auth.application

import com.practice.kopring.common.exception.user.InvalidUserRoleException
import com.practice.kopring.user.enumerate.Role
import org.apache.logging.log4j.kotlin.Logging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class JwtTokenProviderTests {
    companion object : Logging

    private val accessTokenExpiredTime: Long = 1500
    private val refreshTokenExpiredTime: Long = 3000
    private val auth0JwtTokenProvider: Auth0JwtTokenProvider = Auth0JwtTokenProvider(
        "qqweqwewqewe", "hahaha",
        accessTokenExpiredTime, refreshTokenExpiredTime
    )

    @Test
    fun `create Access token`() {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        logger.info { "accessToken: ${accessToken}" }
        Assertions.assertThat(accessToken).isNotNull()
        val validate: Boolean = this.auth0JwtTokenProvider.validate(accessToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertThat(validate).isTrue()
    }

    @Test
    fun `create Refresh token`() {
        val refreshToken: String = this.auth0JwtTokenProvider.createRefreshToken("PAYLOAD")
        logger.info { "refreshToken: ${refreshToken}" }
        Assertions.assertThat(refreshToken).isNotNull()
        val validate: Boolean = this.auth0JwtTokenProvider.validate(refreshToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertThat(validate).isTrue()
    }

    @Test
    fun `token authentication test`() {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val authentication: Role = this.auth0JwtTokenProvider.getRole(accessToken)
        logger.info { "authentication: ${authentication.key}" }
        Assertions.assertThat(authentication.key)
            .isEqualTo(Role.USER.key)
    }

    @Test
    fun `token authentication null test`() {
        Assertions.assertThatThrownBy {
            this.auth0JwtTokenProvider.getRole("asdsd")
        }.isInstanceOf(InvalidUserRoleException::class.java)
    }

    @Test
    fun `token authentication expired test`() {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val expiration: Long = this.auth0JwtTokenProvider.getExpiration(accessToken)
        Assertions.assertThat(expiration).isGreaterThan(this.accessTokenExpiredTime - 1000)
    }

    @Test
    fun `token authentication expired null test`() {
        val expiration: Long = this.auth0JwtTokenProvider.getExpiration("asdsd")
        Assertions.assertThat(expiration).isEqualTo(-1L)
    }
}
