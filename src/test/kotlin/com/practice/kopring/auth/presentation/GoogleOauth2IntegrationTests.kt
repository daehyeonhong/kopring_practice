package com.practice.kopring.user.application

import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class GoogleOauth2IntegrationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var oAuth2AuthorizedClientService: OAuth2AuthorizedClientService

    @MockBean
    private lateinit var clientRegistrationRepository: ClientRegistrationRepository

    @MockBean
    private lateinit var oAuth2UserService: CustomOAuth2UserService

    val oAuth2User = DefaultOAuth2User(
        AuthorityUtils.createAuthorityList("ROLE_USER"),
        mapOf("name" to "testUser"),
        "name"
    )

    @Test
    @Disabled
    @WithMockUser
    fun `Google OAuth2 sign-up or update user information`() {
        // Set up existing user or new user
        val email = "test@example.com"
        val userEntity = UserEntity(
            name = "test",
            email = email,
            picture = "test_picture",
            role = Role.USER,
            provider = Provider.GOOGLE,
            oAuth2Id = "123456789"
        )
        Mockito.`when`(oAuth2UserService.loadUser(Mockito.any())).thenReturn(oAuth2User)
        Mockito.`when`(userRepository.findByEmail(email)).thenReturn(userEntity)

        // Perform GET request
        mockMvc.perform(
            get("/login/oauth2/code/google")
                .accept(MediaType.APPLICATION_JSON)
                .with(oauth2Login().oauth2User(oAuth2User))
        )
            .andExpect(status().isFound)
            .andDo(
                MockMvcRestDocumentation.document(
                    "{class-name}/{method-name}",
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("accessToken").type(JsonFieldType.STRING)
                            .description("액세스토큰"),
                        PayloadDocumentation.fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                            .description("갱신토큰")
                    )
                )
            )

        // Verify the userRepository interactions
//        Mockito.verify(userRepository).findByEmail(email)
//        Mockito.verify(userRepository).save(Mockito.any(UserEntity::class.java))
    }

    fun createTestOAuth2AuthorizationExchange(): OAuth2AuthorizationExchange {
        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .authorizationUri("https://authorization-server.com/oauth/authorize")
            .clientId("client_id")
            .redirectUri("https://client.example.com/cb")
            .scope("read", "write")
            .state("state")
            .build()

        val authorizationResponse = OAuth2AuthorizationResponse.success("code")
            .redirectUri("https://client.example.com/cb")
            .state("state")
            .build()

        return OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse)
    }

}
