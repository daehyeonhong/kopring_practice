package com.practice.kopring.auth.application

import com.practice.kopring.common.exception.auth.TokenInvalidException
import com.practice.kopring.user.enumerate.Role
import org.apache.logging.log4j.kotlin.Logging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication

class OktaJwtTokenProviderTests {
    companion object : Logging

    private val accessTokenExpiredTime: Long = 1500
    private val refreshTokenExpiredTime: Long = 3000
    private val secretKey: String = "its_secret_key_for_test_only"
    private val issuer: String = "http://localhost:8080"
    private val validAuth0JwtTokenProvider: OktaJwtTokenProvider =
        OktaJwtTokenProvider(
            secret = this.secretKey,
            issuer = this.issuer,
            accessTokenExpiredTime = this.accessTokenExpiredTime,
            refreshTokenExpiredTime = this.refreshTokenExpiredTime
        )
    private val invalidAuth0JwtTokenProvider: OktaJwtTokenProvider =
        OktaJwtTokenProvider(
            secret = this.secretKey,
            issuer = this.issuer,
            accessTokenExpiredTime = 0,
            refreshTokenExpiredTime = 0
        )

    @Test
    fun `create Access token`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        logger.info { "accessToken: ${accessToken}" }
        Assertions.assertThat(accessToken).isNotNull()
        val validate: Boolean = this.validAuth0JwtTokenProvider.validate(accessToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertThat(validate).isTrue()
    }

    @Test
    fun `create Refresh token`() {
        val refreshToken: String = this.validAuth0JwtTokenProvider.createRefreshToken("PAYLOAD")
        logger.info { "refreshToken: ${refreshToken}" }
        Assertions.assertThat(refreshToken).isNotNull()
        val validate: Boolean = this.validAuth0JwtTokenProvider.validate(refreshToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertThat(validate).isTrue()
    }

    @Test
    fun `token authentication test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val authentication: Role = this.validAuth0JwtTokenProvider.getRole(accessToken)
        logger.info { "authentication: ${authentication.key}" }
        Assertions.assertThat(authentication.key)
            .isEqualTo(Role.USER.key)
    }

    @Test
    fun `token authentication null test`() {
        Assertions.assertThatThrownBy {
            this.validAuth0JwtTokenProvider.getRole("asdsd")
        }.isInstanceOf(TokenInvalidException::class.java)
    }

    @Test
    fun `token authentication expired test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val expiration: Long = this.validAuth0JwtTokenProvider.getExpiration(accessToken)
        Assertions.assertThat(expiration).isGreaterThan(this.accessTokenExpiredTime - 1000)
    }

    @Test
    fun `token authentication expired Exception test`() {
        val accessToken: String = this.invalidAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        Assertions.assertThatThrownBy {
            this.validAuth0JwtTokenProvider.getExpiration(accessToken)
        }.isInstanceOf(TokenInvalidException::class.java)
    }

    @Test
    fun `token authentication validate Exception test`() {
        val accessToken: String = this.invalidAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        Assertions.assertThatThrownBy {
            this.validAuth0JwtTokenProvider.validate(accessToken)
        }.isInstanceOf(TokenInvalidException::class.java)
    }

    @Test
    fun `token subject test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)

        this.validAuth0JwtTokenProvider.getSubject(accessToken)

        Assertions.assertThat(this.validAuth0JwtTokenProvider.getSubject(accessToken))
            .isEqualTo("PAYLOAD")
    }

    @Test
    fun `token emptySubject test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("", Role.USER)
        Assertions.assertThatThrownBy {
            this.validAuth0JwtTokenProvider.getSubject(accessToken)
        }.isInstanceOf(TokenInvalidException::class.java)
    }

    @Test
    fun `refreshTokenExpiredTime Test`() {
        Assertions.assertThat(this.validAuth0JwtTokenProvider.refreshTokenExpireTime())
            .isEqualTo(this.refreshTokenExpiredTime)
    }

    @Test
    fun `getAuthentication Test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val authentication: Authentication = this.validAuth0JwtTokenProvider.getAuthentication(accessToken)
        logger.info { "authentication: ${authentication}" }
        Assertions.assertThat(authentication).isNotNull()
    }

    @Test
    fun `getAuthentication Exception Test`() {
        val accessToken: String = this.validAuth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val authentication = this.validAuth0JwtTokenProvider.getAuthentication(accessToken)
        logger.info { "authentication: ${authentication}" }
        Assertions.assertThat(authentication).isNotNull()
    }
}
