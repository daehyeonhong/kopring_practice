package com.practice.kopring.auth.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.practice.kopring.user.domain.enumerate.Role
import java.util.Collections
import java.util.Date
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
        private const val ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1_000L * 24 * 60
        private const val REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 1_000L * 24 * 60
    }

    fun createAccessToken(id: String, role: Role): String {
        return JWT.create()
            .withSubject(id)
            .withClaim("role", role.key)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
            .sign(Algorithm.HMAC512(this.secretKey))
    }

    fun createRefreshToken(id: String): String {
        return JWT.create()
            .withSubject(id)
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
            .sign(Algorithm.HMAC512(this.secretKey))
    }

    fun validate(token: String): Boolean {
        return this.decodedJWT(token)?.expiresAt?.after(Date()) ?: false
    }

    private fun decodedJWT(token: String): DecodedJWT? {
        return JWT.require(Algorithm.HMAC512(this.secretKey)).build()
            .verify(token)
    }

    fun getAccountName(token: String): String? {
        return this.decodedJWT(token)?.subject
    }

    fun getAuthentication(token: String): Authentication {
        return UsernamePasswordAuthenticationToken(
            this.getAccountName(token),
            null,
            this.getAuthorities(token)
        )
    }

    private fun getAuthorities(token: String): Collection<GrantedAuthority>? {
        val jwt: DecodedJWT? = this.decodedJWT(token)
        return Collections.singletonList(SimpleGrantedAuthority(jwt?.claims?.get("role").toString()))
    }

    fun getExpiration(accessToken: String): Long {
        val expiration: Date? = this.decodedJWT(accessToken)?.expiresAt
        return if (expiration != null) expiration.time - System.currentTimeMillis()
        else -1L
    }

    fun refreshTokenExpireTime(): Long = REFRESH_TOKEN_EXPIRE_TIME
}
