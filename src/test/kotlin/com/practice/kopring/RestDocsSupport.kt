package com.practice.kopring

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
abstract class RestDocsSupport {
    protected val objectMapper: ObjectMapper = ObjectMapper()
    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(
        restDocumentationContextProvider: RestDocumentationContextProvider
    ) {
        this.mockMvc =
            MockMvcBuilders
                .standaloneSetup(initController())
                .apply<StandaloneMockMvcBuilder>(
                    MockMvcRestDocumentation.documentationConfiguration(
                        restDocumentationContextProvider
                    )
                )
                .build()
    }

    protected abstract fun initController(): Any
}
