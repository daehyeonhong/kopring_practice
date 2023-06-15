package com.practice.kopring.common.util

import com.practice.kopring.auth.enumerate.Token
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie

class CookieUtils {
    companion object {
        private fun getCookie(request: HttpServletRequest, name: String): Cookie? =
            request.cookies?.firstOrNull { it.name == name }

        private fun deleteCookie(vararg names: String): Unit = names.forEach(this::addCookie)

        fun addCookie(
            name: String,
            value: String = "",
            maxAge: Long = 0
        ): ResponseCookie = ResponseCookie.from(name, value)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(maxAge)
            .build()

        fun clearTokenCookies(response: HttpServletResponse): Unit {
            deleteCookie(Token.ACCESS_TOKEN.value, Token.REFRESH_TOKEN.value)
        }
    }
}
