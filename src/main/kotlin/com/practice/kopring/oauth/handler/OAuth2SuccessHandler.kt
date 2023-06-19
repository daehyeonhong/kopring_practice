package com.practice.kopring.oauth.handler

import com.github.f4b6a3.ulid.UlidCreator
import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.dto.OneTimeToken
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*

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
            val user: UserEntity = this.userService.findByEmail(email)
            val oneTimeTokenId: String = UlidCreator.getMonotonicUlid().toUuid().toString()

            this.userRedisCacheService.saveOneTimeToken(
                oneTimeToken = OneTimeToken(
                    oneTimeTokenId = oneTimeTokenId,
                    userId = user.id.toString()
                ), expiredTime = 30000
            )

            val redirectUrl = UriComponentsBuilder.fromUri(URI("$redirectUrl/$provider"))
                .queryParam("oneTimeToken", oneTimeTokenId)
                .build(true).toString()

            response.sendRedirect(redirectUrl)
        }
    }
}
