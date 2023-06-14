package com.practice.kopring.auth.presentation

import com.practice.kopring.MvcRestDocsConfiguration
import com.practice.kopring.auth.application.AuthService
import com.practice.kopring.auth.dto.JwtTokenResponse
import com.practice.kopring.auth.enumerate.Token
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureRestDocs
@WebMvcTest(AuthController::class)
@Import(MvcRestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class)
class AuthControllerMockTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authService: AuthService

    @Disabled
    @Test
    @WithMockUser
    fun refreshToken(): Unit {
        given(this.authService.refresh(anyString()))
            .willReturn(JwtTokenResponse("accessToken", "refreshToken"))
        mockMvc.perform(
            get("/auth/refresh")
                .header("refresh_token", "refreshToken")
        )
            .andExpect {
                status().isOk()
                jsonPath("$.accessToken").value("accessToken")
                jsonPath("$.refreshToken").value("refreshToken")
            }
            .andDo(
                document(
                    "{class-name}/{method-name}",
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스토큰"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("갱신토큰")
                    )
                )
            )
    }

    @Test
    @Disabled
    @WithMockUser
    fun logout(): Unit {
        doNothing().`when`(this.authService).revokeToken(
            anyString()
        )
        mockMvc.perform(
            get("/auth/logout")
                .header("Authorization", "${Token.BEARER_PREFIX.value}dummyToken")
        )
            .andExpect(status().isNoContent)
            .andDo(document("{class-name}/{method-name}"))
    }

    @Test
    @Disabled
    @WithMockUser
    fun header(): Unit {
        doNothing().`when`(this.authService).revokeToken(
            anyString()
        )
        mockMvc.perform(
            get("/auth/logout")
                .header("Authorization", "${Token.BEARER_PREFIX.value}dummyToken")
        )
            .andExpect(status().isNoContent)
            .andDo(document("{class-name}/{method-name}"))
    }
}
