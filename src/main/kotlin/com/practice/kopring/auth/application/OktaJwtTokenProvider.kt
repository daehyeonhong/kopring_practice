package com.practice.kopring.auth.application

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority

class OktaJwtTokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.secret}") private val issuer: String,
) {
    companion object : Logging {
        private const val DAY: Long = 24 * 60 * 60 * 1_000L
        private const val WEEK: Long = 7 * DAY
        private const val ACCESS_TOKEN_EXPIRED_TIME: Long = 1 * DAY
        private const val REFRESH_TOKEN_EXPIRED_WEEK: Long = 1 * WEEK
    }

    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(this.secret.toByteArray()).toByteArray())

    fun createAccessToken(id: String, roles: Collection<GrantedAuthority>): String {
        val claims: Claims = Jwts.claims().let {
            it.issuer = this.issuer
            it.subject = id
            it.issuedAt = Date(System.currentTimeMillis())
            it.expiration = Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME)
            it["roles"] = roles
            it
        }

        return Jwts.builder()
            .setClaims(claims)
            .signWith(this.secretKey)
            .compact()
    }

    fun createRefreshToken(id: String): String {
        val claims: Claims = Jwts.claims().let {
            it.issuer = this.issuer
            it.subject = id
            it
        }

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_WEEK))
            .signWith(this.secretKey)
            .compact()
    }

    fun isValidateToken(token: String?): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (jwtException: JwtException) {
            logger.error { jwtException.message }
            false
        }
    }

}
