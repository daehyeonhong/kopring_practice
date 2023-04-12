package com.practice.kopring.auth.application

import com.practice.kopring.common.exception.auth.TokenInvalidException
import com.practice.kopring.user.enumerate.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class OktaJwtTokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.access_token}") private val accessTokenExpiredTime: Long,
    @Value("\${jwt.refresh_token}") private val refreshTokenExpiredTime: Long,
) : JwtTokenProvider {
    companion object : Logging;

    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(this.secret.toByteArray()).toByteArray())

    override fun createAccessToken(id: String, role: Role): String = Jwts.builder()
        .setIssuer(this.issuer)
        .setSubject(id)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + this.accessTokenExpiredTime))
        .claim("role", role)
        .signWith(this.secretKey)
        .compact()

    override fun createRefreshToken(id: String): String = Jwts.builder()
        .setIssuer(this.issuer)
        .setSubject(id)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + this.refreshTokenExpiredTime))
        .signWith(this.secretKey)
        .compact()

    override fun validate(token: String?): Boolean = this.verifyToken(token).expiration.before(Date())
    override fun getSubject(token: String?): String = this.verifyToken(token).subject
    override fun refreshTokenExpireTime(): Long = this.refreshTokenExpiredTime
    override fun getExpiration(token: String?): Long = this.verifyToken(token).expiration.time
    override fun getRole(token: String?): Role = Role.of(this.verifyToken(token).role)
    override fun getAuthentication(token: String): Authentication = UsernamePasswordAuthenticationToken(
        this.getSubject(token), null, this.getAuthorities(token)
    )

    private fun getAuthorities(token: String): Collection<GrantedAuthority> =
        setOf(SimpleGrantedAuthority(this.getRole(token).key))

    private fun verifyToken(token: String?): JwtTokenDto = try {
        JwtTokenDto.fromClaims(
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
        )
    } catch (jwtException: JwtException) {
        logger.error { jwtException.message }
        throw TokenInvalidException()
    }

    data class JwtTokenDto(
        val subject: String,
        val expiration: Date,
        val role: String,
    ) {
        companion object {
            fun fromClaims(claims: Jws<Claims>): JwtTokenDto = JwtTokenDto(
                subject = claims.body.subject,
                expiration = claims.body.expiration,
                role = claims.body["role"].toString()
            )
        }
    }
}
