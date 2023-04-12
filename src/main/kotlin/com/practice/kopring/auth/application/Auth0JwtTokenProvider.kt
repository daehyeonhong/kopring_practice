package com.practice.kopring.auth.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import com.practice.kopring.user.enumerate.Role
import java.util.*
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class Auth0JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.secret}") private val issuer: String,
) : JwtTokenProvider {
    companion object : Logging {
        private const val DAY: Long = 24 * 60 * 60 * 1_000L
        private const val WEEK: Long = 7 * DAY
        private const val ACCESS_TOKEN_EXPIRED_TIME: Long = 1 * DAY
        private const val REFRESH_TOKEN_EXPIRED_WEEK: Long = 1 * WEEK
    }

    private val algorithm: Algorithm = Algorithm.HMAC512(this.secretKey)

    override fun createAccessToken(id: String, role: Role): String {
        return JWT.create()
            .withSubject(id)
            .withIssuer(this.issuer)
            .withClaim("role", role.key)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME))
            .sign(this.algorithm)
    }

    override fun createRefreshToken(id: String): String {
        return JWT.create()
            .withSubject(id)
            .withIssuer(this.issuer)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_WEEK))
            .sign(this.algorithm)
    }

    override fun validate(token: String?): Boolean = this.decodedJWT(token)?.expiresAt?.after(Date()) ?: false

    private fun decodedJWT(token: String?): DecodedJWT? = try {
        JWT.require(this.algorithm).build().verify(token)
    } catch (jwtDecodeException: JWTDecodeException) {
        logger.error { "JWTDecodeException: ${jwtDecodeException.message}" }
        null
    }

    override fun getUserId(token: String?): String? = this.decodedJWT(token)?.subject

    override fun getAuthentication(token: String): Authentication = UsernamePasswordAuthenticationToken(
        this.getUserId(token), null, this.getAuthorities(token)
    )

    private fun getAuthorities(token: String): Collection<GrantedAuthority>? {
        val jwt: DecodedJWT? = this.decodedJWT(token)
        return Collections.singletonList(SimpleGrantedAuthority(jwt?.claims?.get("role").toString()))
    }

    override fun getExpiration(accessToken: String): Long {
        val expiration: Date? = this.decodedJWT(accessToken)?.expiresAt
        return expiration?.let { it.time - System.currentTimeMillis() } ?: -1L
    }

    override fun refreshTokenExpireTime(): Long = REFRESH_TOKEN_EXPIRED_WEEK
}
