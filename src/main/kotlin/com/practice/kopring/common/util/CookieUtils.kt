package com.practice.kopring.common.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie

object CookieUtils {
    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == name }
    }

    fun addCookie(
        name: String,
        value: String,
        maxAge: Long
    ): ResponseCookie =
        ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(maxAge)
            .build()

    fun deleteCookie(
        response: HttpServletResponse,
        name: String
    ) {
        val cookie = Cookie(name, "")
        cookie.path = "/"
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    fun getValue(request: HttpServletRequest, name: String): String? =
        getCookie(request, name)?.value?.ifBlank { null }
}
