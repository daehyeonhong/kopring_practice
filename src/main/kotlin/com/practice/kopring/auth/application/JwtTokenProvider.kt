package com.practice.kopring.auth.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.user.enumerate.Role
import jakarta.servlet.http.HttpServletRequest
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String
) {
    companion object {
        private const val DAY: Long = 24 * 60 * 60 * 1_000L
        private const val WEEK: Long = 7 * DAY
        private const val ACCESS_TOKEN_EXPIRED_TIME: Long = 1 * DAY
        private const val REFRESH_TOKEN_EXPIRED_WEEK: Long = 1 * WEEK
    }

    private val algorithm: Algorithm = Algorithm.HMAC512(this.secretKey)

    fun createAccessToken(id: String, role: Role): String {
        return JWT
            .create()
            .withSubject(id)
            .withClaim("role", role.key)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME))
            .sign(this.algorithm)
    }

    fun createRefreshToken(id: String): String {
        return JWT.create().withSubject(id).withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_WEEK))
            .sign(this.algorithm)
    }

    fun validate(token: String?): Boolean = this.decodedJWT(token)?.expiresAt?.after(Date()) ?: false

    private fun decodedJWT(token: String?): DecodedJWT? {
        return JWT.require(this.algorithm).build().verify(token)
    }

    fun getUserId(token: String?): String? = this.decodedJWT(token)?.subject

    fun getAuthentication(token: String): Authentication = UsernamePasswordAuthenticationToken(
        this.getUserId(token), null, this.getAuthorities(token)
    )

    private fun getAuthorities(token: String): Collection<GrantedAuthority>? {
        val jwt: DecodedJWT? = this.decodedJWT(token)
        return Collections.singletonList(SimpleGrantedAuthority(jwt?.claims?.get("role").toString()))
    }

    fun getExpiration(accessToken: String): Long {
        val expiration: Date? = this.decodedJWT(accessToken)?.expiresAt
        return expiration?.let { it.time - System.currentTimeMillis() } ?: -1L
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return this.resolveToken(request.getHeader(Token.AUTHORIZATION_HEADER.value))
    }

    fun resolveToken(bearerToken: String?): String? {
        return when {
            !bearerToken.isNullOrBlank() && bearerToken.startsWith(Token.BEARER_PREFIX.value) ->
                bearerToken.replace(Token.BEARER_PREFIX.value, "")

            else -> null
        }
    }

    fun refreshTokenExpireTime(): Long = REFRESH_TOKEN_EXPIRED_WEEK
}
