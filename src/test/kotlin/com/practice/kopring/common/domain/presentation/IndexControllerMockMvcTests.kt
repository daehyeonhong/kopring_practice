package com.practice.kopring.common.domain.presentation

import com.practice.kopring.RestDocsTestSupport
import com.practice.kopring.common.presentation.IndexController
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(value = [IndexController::class])
class IndexControllerMockMvcTests : RestDocsTestSupport() {
    @Test
    @WithMockUser
    fun oauth2() {
        mockMvc.perform(get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
//        getA("/")
    }
}
