package com.practice.kopring.auth.application

import com.practice.kopring.common.exception.auth.TokenInvalidException
import com.practice.kopring.user.enumerate.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
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
        .claim("role", role.key)
        .signWith(this.secretKey)
        .compact()

    override fun createRefreshToken(id: String): String = Jwts.builder()
        .setIssuer(this.issuer)
        .setSubject(id)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + this.refreshTokenExpiredTime))
        .signWith(this.secretKey)
        .compact()

    override fun validate(token: String?): Boolean = this.verifyToken(token).expiration.after(Date())
    override fun getSubject(token: String?): String =
        this.verifyToken(token).subject.ifBlank { throw TokenInvalidException() }

    override fun refreshTokenExpireTime(): Long = this.refreshTokenExpiredTime
    override fun getExpiration(token: String?): Long = this.verifyToken(token).expiration.time
    override fun getRole(token: String?): Role = Role.of(this.verifyToken(token).role)
    override fun getAuthentication(token: String): Authentication {
        val verifyToken: JwtTokenDto = this.verifyToken(token)
        return UsernamePasswordAuthenticationToken(
            verifyToken.subject, null, setOf(SimpleGrantedAuthority(Role.of(verifyToken.role).key))
        )
    }

    private fun verifyToken(token: String?): JwtTokenDto = try {
        JwtTokenDto.fromClaims(
            Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
        )
    } catch (jwtException: RuntimeException) {
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
