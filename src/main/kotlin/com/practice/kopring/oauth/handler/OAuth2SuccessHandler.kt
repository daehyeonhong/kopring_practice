package com.practice.kopring.oauth.handler

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.common.util.CookieUtils
import com.practice.kopring.user.application.CustomOAuth2UserService
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.application.UserService
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    @Value("\${url.redirect}") private val redirectUrl: String,
    private val userService: UserService,
    private val oAuth2UserService: CustomOAuth2UserService,
    private val userRedisCacheService: UserRedisCacheService,
    private val jwtTokenProvider: JwtTokenProvider
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val oAuth2User: OAuth2User = authentication?.principal as OAuth2User

        val email: String = oAuth2User.attributes["email"] as String
        val provider: String = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId
        this.oAuth2UserService.saveOrUpdate(oAuth2User, Provider.of(provider))

        val user: UserEntity = this.userService.findByEmail(email)

        val id: String = user.id.toString()
        val accessToken: String = this.jwtTokenProvider.createAccessToken(id, user.role)
        val refreshToken: String = this.jwtTokenProvider.createRefreshToken(id)

        this.userRedisCacheService.save(
            RefreshToken(refreshToken, id),
            this.jwtTokenProvider.refreshTokenExpireTime()
        )

        CookieUtils.addCookie(
            response, Token.ACCESS_TOKEN.value, accessToken,
            this.jwtTokenProvider.getExpiration(accessToken).toInt()
        )

        CookieUtils.addCookie(
            response, Token.REFRESH_TOKEN.value, refreshToken,
            this.jwtTokenProvider.getExpiration(refreshToken).toInt()
        )

        response.sendRedirect(this.redirectUrl)
    }
}
