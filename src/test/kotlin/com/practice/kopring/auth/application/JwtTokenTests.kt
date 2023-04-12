package com.practice.kopring.auth.application

import com.practice.kopring.user.enumerate.Role
import java.util.*
import org.apache.logging.log4j.kotlin.Logging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtTokenTests {
    companion object : Logging

    private val auth0JwtTokenProvider: Auth0JwtTokenProvider = Auth0JwtTokenProvider("qqweqwewqewe", "hahaha")

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
        val authentication: Authentication = this.auth0JwtTokenProvider.getAuthentication(accessToken)
        logger.info { "authentication: ${authentication.authorities.first().authority}" }
        val comepare: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
            "PAYLOAD", null, Collections.singletonList(SimpleGrantedAuthority("\"${Role.USER.key}\""))
        )
        Assertions.assertThat(authentication.authorities.first().authority)
            .isEqualTo(comepare.authorities.first().authority)
    }

    @Test
    fun `token authentication null test`() {
        val authentication: Authentication = this.auth0JwtTokenProvider.getAuthentication("asdsd")
        logger.info { "authentication: ${authentication.authorities.first().authority}" }
        Assertions.assertThat(authentication.authorities.first().authority).isEqualTo("null")
    }

    @Test
    fun `token authentication expired test`() {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        val expiration: Long = this.auth0JwtTokenProvider.getExpiration(accessToken)
        Assertions.assertThat(expiration).isGreaterThan(24 * 60 * 60 * 1000L - 1000)
    }

    @Test
    fun `token authentication expired null test`() {
        val expiration: Long = this.auth0JwtTokenProvider.getExpiration("asdsd")
        Assertions.assertThat(expiration).isEqualTo(-1L)
    }

    @Test
    fun `token refresh expireTime test`() {
        Assertions.assertThat(this.auth0JwtTokenProvider.refreshTokenExpireTime()).isEqualTo(7 * 24 * 60 * 60 * 1000L)
    }

}
