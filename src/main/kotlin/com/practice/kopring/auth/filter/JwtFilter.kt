package com.practice.kopring.auth.filter

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.user.application.UserRedisCacheService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRedisCacheService: UserRedisCacheService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = this.resolveToken(request)
        if (!token.isNullOrBlank() && this.jwtTokenProvider.validate(token)) {
            val isLogout: String? = this.userRedisCacheService.getWithToken(token)
            if (isLogout.isNullOrBlank()) {
                val authentication: Authentication = this.jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader(Token.AUTHORIZATION_HEADER.value)
        return when {
            !bearerToken.isNullOrBlank() && bearerToken.startsWith(Token.BEARER_PREFIX.value) ->
                bearerToken.replace(Token.BEARER_PREFIX.value, "")

            else -> null
        }
    }
}
