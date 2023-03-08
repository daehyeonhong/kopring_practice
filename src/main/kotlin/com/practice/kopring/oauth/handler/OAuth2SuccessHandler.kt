package com.practice.kopring.oauth.handler

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.domain.RefreshToken
import com.practice.kopring.user.application.CustomOAuth2UserService
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.application.UserService
import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.domain.enumerate.Status
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
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
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val oAuth2User: OAuth2User = authentication?.principal as OAuth2User
        val email: String = oAuth2User.attributes["email"] as String
        val status: Status = Status.of(this.userService.checkExistEmail(email))

        this.oAuth2UserService.saveOrUpdate(oAuth2User)

        val user: UserEntity = this.userService.findByEmail(email)

        val id: String = user.id.toString()
        val accessToken: String = this.jwtTokenProvider.createAccessToken(id, user.role)
        val refreshToken: String = this.jwtTokenProvider.createRefreshToken(id);

        this.userRedisCacheService.save(
            RefreshToken(refreshToken, id),
            this.jwtTokenProvider.refreshTokenExpireTime()
        )

        val redirectUrl: String = "${this.redirectUrl}?status=${status}?access=${accessToken}?refresh=${refreshToken}"
        response?.sendRedirect(redirectUrl)
    }
}
