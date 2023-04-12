package com.practice.kopring.auth.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import com.practice.kopring.common.exception.user.NotExistsUserException
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
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.access_token}") private val accessTokenExpiredTime: Long,
    @Value("\${jwt.refresh_token}") private val refreshTokenExpiredTime: Long,
) : JwtTokenProvider {
    companion object : Logging

    private val algorithm: Algorithm = Algorithm.HMAC512(this.secretKey)

    override fun createAccessToken(id: String, role: Role): String {
        return JWT.create()
            .withSubject(id)
            .withIssuer(this.issuer)
            .withClaim("role", role.key)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + this.accessTokenExpiredTime))
            .sign(this.algorithm)
    }

    override fun createRefreshToken(id: String): String {
        return JWT.create()
            .withSubject(id)
            .withIssuer(this.issuer)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + this.refreshTokenExpiredTime))
            .sign(this.algorithm)
    }

    override fun validate(token: String?): Boolean = this.decodedJWT(token).expiresAt?.after(Date()) ?: false
    override fun getSubject(token: String?): String = this.decodedJWT(token).subject ?: throw NotExistsUserException()
    override fun refreshTokenExpireTime(): Long = this.refreshTokenExpiredTime
    override fun getAuthentication(token: String): Authentication = UsernamePasswordAuthenticationToken(
        this.getSubject(token), null, this.getAuthorities(token)
    )

    private fun getAuthorities(token: String): Collection<GrantedAuthority> {
        val jwt: DecodedJWT = this.decodedJWT(token)
        return setOf(SimpleGrantedAuthority(Role.of(jwt.claims?.get("role").toString()).key))
    }

    override fun getExpiration(token: String?): Long =
        decodedJWT(token).expiresAt?.let { it.time - System.currentTimeMillis() } ?: -1L

    override fun getRole(token: String?): Role = Role.of(decodedJWT(token).getClaim("role")?.asString())

    private fun decodedJWT(token: String?): DecodedJWT =
        JWT.require(this.algorithm).build().verify(token) ?: throw JWTDecodeException("Invalid token")
}
