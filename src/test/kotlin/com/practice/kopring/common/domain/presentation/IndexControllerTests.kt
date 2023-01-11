package com.practice.kopring.common.domain.presentation

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(value = [IndexController::class])
class IndexControllerTests(@Autowired private val mvc: MockMvc) {

    @Test
    fun index() {
        mvc.perform(get("/"))
            .andExpect(status().isOk)
    }
}
