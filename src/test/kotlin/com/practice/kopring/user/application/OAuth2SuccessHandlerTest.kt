package com.practice.kopring.user.application

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.oauth.handler.OAuth2SuccessHandler
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [OAuth2SuccessHandler::class])
class OAuth2SuccessHandlerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockBean private val userService: UserService,
    @MockBean private val oAuth2UserService: CustomOAuth2UserService,
    @MockBean private val userRedisCacheService: UserRedisCacheService,
    @MockBean private val jwtTokenProvider: JwtTokenProvider
) {
    @Test
    @Disabled
    @WithMockUser
    fun `test onAuthenticationSuccess`() {
        val email = "test@example.com"
        val user = UserEntity(
            email = email,
            role = Role.USER,
            provider = Provider.GOOGLE,
            name = "김개똥",
            picture = "https://example.com",
            oAuth2Id = "sample_oauth2_id"
        )

        `when`(userService.findByEmail(email)).thenReturn(user)
        `when`(
            oAuth2UserService.saveOrUpdate(
                eq(
                    DefaultOAuth2User(
                        setOf(SimpleGrantedAuthority(Role.USER.key)), mapOf("nameAttribute" to anyString()), anyString()
                    )
                ), eq(Provider.GOOGLE)
            )
        ).thenReturn(user)
        `when`(jwtTokenProvider.createAccessToken(anyString(), eq(Role.USER))).thenReturn("sample_access_token")
        `when`(jwtTokenProvider.createRefreshToken(anyString())).thenReturn("sample_refresh_token")
        `when`(jwtTokenProvider.getExpiration(anyString())).thenReturn(3600)

        val authority = OAuth2UserAuthority(mapOf("email" to email))
        val oAuth2User = DefaultOAuth2User(setOf(authority), authority.attributes, "email")
        val authenticationToken = mock(OAuth2AuthenticationToken::class.java)
        `when`(authenticationToken.authorizedClientRegistrationId).thenReturn("sample_client_id")
        `when`(authenticationToken.principal).thenReturn(oAuth2User)

        mockMvc.perform(
            get("/oauth2/redirect").requestAttr("authentication", authenticationToken)
        ).andExpect(status().is3xxRedirection).andExpect { result ->
            val cookies = result.response.cookies
            val accessToken = cookies.find { it.name == "ACCESS_TOKEN" }?.value
            val refreshToken = cookies.find { it.name == "REFRESH_TOKEN" }?.value

            assert(accessToken == "sample_access_token")
            assert(refreshToken == "sample_refresh_token")
        }

    }
}
