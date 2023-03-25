package com.practice.kopring.common.domain.presentation

import com.practice.kopring.MockMvcTestBase
import com.practice.kopring.user.presentation.UserController
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(value = [UserController::class])
class UserControllerMockMvcTests : MockMvcTestBase() {
    @Test
    fun oauth2() {
        get("/login/oauth2/code/google")
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcRestDocumentation.document("get-user"))
    }
}
