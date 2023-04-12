package com.practice.kopring.auth.application

import com.practice.kopring.user.enumerate.Role
import org.springframework.security.core.Authentication

interface JwtTokenProvider {
    fun createAccessToken(id: String, role: Role): String
    fun createRefreshToken(id: String): String
    fun validate(token: String?): Boolean
    fun getSubject(token: String?): String
    fun getExpiration(token: String?): Long
    fun getRole(token: String?): Role
    fun refreshTokenExpireTime(): Long
    fun getAuthentication(token: String): Authentication
}
