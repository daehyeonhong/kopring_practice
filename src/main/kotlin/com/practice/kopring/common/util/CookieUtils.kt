package com.practice.kopring.common.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CookieUtils {
    companion object {
        private fun getCookie(request: HttpServletRequest, cookieName: String): Cookie? =
            request.cookies?.firstOrNull { it.name == cookieName }

        fun addCookie(
            response: HttpServletResponse,
            name: String,
            value: String,
            maxAge: Int = 0
        ) {
            Cookie(name, value).let { it: Cookie ->
                it.secure = true
                it.isHttpOnly = true
                it.path = "/"
                it.maxAge = maxAge
                response.addCookie(it)
            }
        }

        fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
            getCookie(request, name)?.let { cookie: Cookie ->
                cookie.secure = true
                cookie.isHttpOnly = true
                cookie.path = "/"
                cookie.maxAge = 0
                cookie.value = ""
                response.addCookie(cookie)
            }
        }
    }
}
