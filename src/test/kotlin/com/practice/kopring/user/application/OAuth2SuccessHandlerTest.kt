package com.practice.kopring.user.application

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.oauth.handler.OAuth2SuccessHandler
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
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
class OAuth2SuccessHandlerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var oAuth2UserService: CustomOAuth2UserService

    @MockBean
    lateinit var userRedisCacheService: UserRedisCacheService

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    @WithMockUser
    fun `test onAuthenticationSuccess`() {
        val email = "test@example.com"
        val user = UserEntity(
            email = email,
            role = Role.USER,
            provider = Provider.GOOGLE,
            name = "김개똥",
            picture = "https://example.com",
        )

        `when`(userService.findByEmail(email)).thenReturn(user)
        `when`(oAuth2UserService.saveOrUpdate(any(), any())).thenReturn(user)
        `when`(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("sample_access_token")
        `when`(jwtTokenProvider.createRefreshToken(any())).thenReturn("sample_refresh_token")
        `when`(jwtTokenProvider.getExpiration(any())).thenReturn(3600)

        val authority = OAuth2UserAuthority(mapOf("email" to email))
        val oAuth2User = DefaultOAuth2User(setOf(authority), authority.attributes, "email")
        val authenticationToken = mock(OAuth2AuthenticationToken::class.java)
        `when`(authenticationToken.authorizedClientRegistrationId).thenReturn("sample_client_id")
        `when`(authenticationToken.principal).thenReturn(oAuth2User)

        mockMvc.perform(
            get("/oauth2/redirect")
                .requestAttr("authentication", authenticationToken)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect { result ->
                val cookies = result.response.cookies
                val accessToken = cookies.find { it.name == "ACCESS_TOKEN" }?.value
                val refreshToken = cookies.find { it.name == "REFRESH_TOKEN" }?.value

                assert(accessToken == "sample_access_token")
                assert(refreshToken == "sample_refresh_token")
            }

    }
}
