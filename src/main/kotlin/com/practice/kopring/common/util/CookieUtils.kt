package com.practice.kopring.common.util

import com.practice.kopring.auth.enumerate.Token
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CookieUtils {
    companion object {
        private fun getCookie(request: HttpServletRequest, name: String): Cookie? =
            request.cookies?.firstOrNull { it.name == name }

        fun addCookie(
            response: HttpServletResponse,
            name: String,
            value: String = "",
            maxAge: Int = 0
        ): Unit {
            Cookie(name, value).let { it: Cookie ->
                it.secure = true
                it.isHttpOnly = true
                it.path = "/"
                it.maxAge = maxAge
                response.addCookie(it)
            }
        }

        private fun deleteCookie(response: HttpServletResponse, vararg names: String): Unit {
            names.forEach { name: String ->
                this.addCookie(response = response, name = name)
            }
        }

        fun clearTokenCookies(response: HttpServletResponse): Unit {
            deleteCookie(response, Token.ACCESS_TOKEN.value, Token.REFRESH_TOKEN.value)
        }
    }
}
