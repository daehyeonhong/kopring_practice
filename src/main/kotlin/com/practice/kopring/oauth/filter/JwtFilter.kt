package com.practice.kopring.oauth.filter

import com.practice.kopring.auth.application.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {
    companion object {
        private const val AUTHORIZATION_HEADER: String = "Authorization"
        private const val BEARER_PREFIX: String = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = this.resolveToken(request)
        if (!token.isNullOrBlank() && this.jwtTokenProvider.validate(token)) {
            val authentication: Authentication = this.jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader(AUTHORIZATION_HEADER)
        return if (!bearerToken.isNullOrBlank() && bearerToken.startsWith(BEARER_PREFIX))
            bearerToken.replace(BEARER_PREFIX, "")
        else null
    }
}
