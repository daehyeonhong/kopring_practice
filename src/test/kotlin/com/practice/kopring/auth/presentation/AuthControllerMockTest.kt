package com.practice.kopring.auth.presentation

import com.practice.kopring.MvcRestDocsConfiguration
import com.practice.kopring.auth.application.AuthService
import com.practice.kopring.auth.dto.JwtTokenResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
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
                    "auth/refresh",
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스토큰"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("갱신토큰")
                    )
                )
            )
    }
}
