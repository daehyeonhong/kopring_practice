package com.practice.kopring.auth.application

import com.practice.kopring.user.enumerate.Role
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JwtTokenTests {
    companion object : Logging

    private val auth0JwtTokenProvider: Auth0JwtTokenProvider = Auth0JwtTokenProvider("qqweqwewqewe", "hahaha")

    @Test
    fun `create Access token`() {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken("PAYLOAD", Role.USER)
        logger.info { "accessToken: ${accessToken}" }
        Assertions.assertNotNull(accessToken)
        val validate: Boolean = this.auth0JwtTokenProvider.validate(accessToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertTrue(validate)
    }

    @Test
    fun `create Refresh token`() {
        val refreshToken: String = this.auth0JwtTokenProvider.createRefreshToken("PAYLOAD")
        logger.info { "refreshToken: ${refreshToken}" }
        Assertions.assertNotNull(refreshToken)
        val validate: Boolean = this.auth0JwtTokenProvider.validate(refreshToken)
        logger.info { "validate: ${validate}" }
        Assertions.assertTrue(validate)
    }

}
