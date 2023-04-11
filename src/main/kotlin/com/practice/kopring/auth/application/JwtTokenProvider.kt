package com.practice.kopring.auth.application

import com.practice.kopring.user.enumerate.Role
import org.springframework.security.core.Authentication

interface JwtTokenProvider {
    fun createAccessToken(id: String, role: Role): String
    fun createRefreshToken(id: String): String
    fun validate(token: String?): Boolean
    fun getUserId(token: String?): String?
    fun getAuthentication(token: String): Authentication
    fun getExpiration(accessToken: String): Long
    fun refreshTokenExpireTime(): Long
}
