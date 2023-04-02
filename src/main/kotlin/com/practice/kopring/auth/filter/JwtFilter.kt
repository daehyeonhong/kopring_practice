package com.practice.kopring.auth.filter

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.user.application.UserRedisCacheService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
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
        val token: String? = this.jwtTokenProvider.resolveToken(request)
        if (!token.isNullOrBlank() && this.jwtTokenProvider.validate(token)) {
            val isLogout: String? = this.userRedisCacheService.getWithToken(token)
            if (isLogout.isNullOrBlank())
                SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(token)
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
        }
        filterChain.doFilter(request, response)
    }
}
