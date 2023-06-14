package com.practice.kopring.common.presentation

import com.practice.kopring.RestDocsTestSupport
import com.practice.kopring.auth.application.AuthService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureRestDocs
@WebMvcTest(controllers = [IndexController::class])
class IndexControllerTests : RestDocsTestSupport() {
    @MockBean
    lateinit var authService: AuthService

    @Test
    @WithMockUser
    fun `test index`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andDo {
                document("index")
            }
    }
}
