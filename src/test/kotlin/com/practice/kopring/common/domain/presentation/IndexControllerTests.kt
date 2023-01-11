package com.practice.kopring.common.domain.presentation

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = [IndexController::class])
class IndexControllerTests(@Autowired private val mvc: MockMvc) {

    @Test
    @DisplayName(value = "권한")
    @WithMockUser(username = "guest", roles = ["USER"])
    fun index() {
        mvc.perform(get("/"))
            .andExpect {
                status().isOk
                content().string("ok")
                println(content())
            }
    }
}
