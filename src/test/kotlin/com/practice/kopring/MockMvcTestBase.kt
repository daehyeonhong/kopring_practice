package com.practice.kopring

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebMvcTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
abstract class MockMvcTestBase {
    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    protected lateinit var objectMapper: ObjectMapper
    private lateinit var restDocumentation: RestDocumentationResultHandler
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        this.restDocumentation = MockMvcRestDocumentation.document(
            "{method-name}",
            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
            Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        )
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
    }

    fun get(uri: String): ResultActions {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDocumentation)
    }

    fun post(uri: String, body: Any): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDocumentation)
    }

    fun put(uri: String, body: Any): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDocumentation)
    }

    fun delete(uri: String): ResultActions {
        return mockMvc.perform(MockMvcRequestBuilders.delete(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDocumentation)
    }

    fun patch(uri: String, body: Any): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(restDocumentation)
    }


}
