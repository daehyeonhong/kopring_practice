package com.practice.kopring.auth

import com.practice.kopring.auth.application.JwtUtil
import com.practice.kopring.user.application.CustomOAuth2UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*


class JwtAuthorizationFilter(
    private val authenticationManager: AuthenticationManager,
    private val oAuth2UserService: CustomOAuth2UserService,
    private val jwtUtil: JwtUtil
) : BasicAuthenticationFilter(authenticationManager) {
    companion object {
        private const val bearer: String = "Bearer "
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader: String? = request.getHeader("Authorization")
        if (authorizationHeader == null || !authorizationHeader.startsWith(bearer)) {
            chain.doFilter(request, response)
            return
        }
        val token: String = authorizationHeader.replace(bearer, "")
        val accountName: String = this.jwtUtil.getAccountNameFromToken(token)
        accountName.let { s ->
            oAuth2UserService.(s)
            val authentication: Authentication = OAuth2AuthenticationToken(
                accountDetail,
                null,
                accountDetail.getAuthorities()
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response);
    }
}
