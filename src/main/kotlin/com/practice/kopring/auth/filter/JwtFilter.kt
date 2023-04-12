package com.practice.kopring.auth.filter

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.user.application.UserRedisCacheService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRedisCacheService: UserRedisCacheService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token: String? = this.resolveToken(request)
        if (!token.isNullOrBlank() && this.jwtTokenProvider.validate(token)) {
            val isLogout: String? = this.userRedisCacheService.getWithToken(token)
            if (isLogout.isNullOrBlank())
                SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(token)
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        return this.resolveToken(request.getHeader(Token.AUTHORIZATION_HEADER.value))
    }

    private fun resolveToken(bearerToken: String?): String? {
        return when {
            !bearerToken.isNullOrBlank() && bearerToken.startsWith(Token.BEARER_PREFIX.value) ->
                bearerToken.replace(Token.BEARER_PREFIX.value, "")

            else -> null
        }
    }
}
