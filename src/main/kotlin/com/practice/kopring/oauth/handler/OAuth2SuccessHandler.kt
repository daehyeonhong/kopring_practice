package com.practice.kopring.oauth.handler

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.common.util.CookieUtils
import com.practice.kopring.user.application.CustomOAuth2UserService
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.application.UserService
import com.practice.kopring.user.enumerate.Provider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class OAuth2SuccessHandler(
    @Value("\${url.redirect}") private val redirectUrl: String,
    private val userService: UserService,
    private val oAuth2UserService: CustomOAuth2UserService,
    private val userRedisCacheService: UserRedisCacheService,
    private val jwtTokenProvider: JwtTokenProvider,
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val authenticationToken = authentication as? OAuth2AuthenticationToken
        val oAuth2User = authenticationToken?.principal
        val email = oAuth2User?.attributes?.get("email") as? String
        val provider = authenticationToken?.authorizedClientRegistrationId

        if (email != null && provider != null) {
            this.oAuth2UserService.saveOrUpdate(oAuth2User, Provider.of(provider))
            val user = this.userService.findByEmail(email)

            val id = user.id.toString()
            val accessToken = this.jwtTokenProvider.createAccessToken(id, user.role)
            val refreshToken = this.jwtTokenProvider.createRefreshToken(id)

            val refreshTokenExpireTime = this.jwtTokenProvider.refreshTokenExpireTime()

            this.userRedisCacheService.save(RefreshToken(refreshToken, id), refreshTokenExpireTime)

            val accessCookie = CookieUtils.addCookie("access_cookie", accessToken, refreshTokenExpireTime).toString()
            val refreshCookie = CookieUtils.addCookie("refresh_cookie", refreshToken, refreshTokenExpireTime).toString()

            response.addHeader("Set-Cookie", accessCookie)
            response.addHeader("Set-Cookie", refreshCookie)

            val redirectUrl = UriComponentsBuilder.fromUri(URI("$redirectUrl/$provider"))
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build(true).toString()

            response.sendRedirect(redirectUrl)
        }
    }
}
