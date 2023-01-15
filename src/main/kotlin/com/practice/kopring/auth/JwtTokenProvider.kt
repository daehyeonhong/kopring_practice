package com.practice.kopring.auth

import com.practice.kopring.auth.domain.dto.JwtDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider {
    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "bearer"
        private const val ACCESS_TOKEN_EXPIRE_TIME: Long = (60L * 30)
        private const val REFRESH_TOKEN_EXPIRE_TIME: Long = (1_000 * 60 * 60 * 24 * 7)
    }

    private val key: Key by lazy<Key> {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode("ZVc3Z0g4bm5TVzRQUDJxUXBIOGRBUGtjRVg2WDl0dzVYVkMyWWs1Qlk3NkZBOXh1UzNoRWUzeTd6cVdEa0x2eQo="))
    }

    fun generateJwtDto(oAuth2User: OAuth2User): JwtDto {
        val now = Date().time
        val accessTokenExpiresIn: Date = Date(now + ACCESS_TOKEN_EXPIRE_TIME)

        val accessToken = Jwts.builder()
            .setSubject(oAuth2User.attributes["email"] as String)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return JwtDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time
        )
    }
}
