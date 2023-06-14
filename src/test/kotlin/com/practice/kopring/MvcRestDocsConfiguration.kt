package com.practice.kopring

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.restdocs.restassured.RestDocumentationFilter

@TestConfiguration
class MvcRestDocsConfiguration {
    @Bean
    fun writeMockMvc(): RestDocumentationResultHandler = MockMvcRestDocumentation.document(
        "{class-name}/{method-name}",
        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    )

    @Bean
    fun writeRestAssured(): RestDocumentationFilter = RestAssuredRestDocumentation.document(
        "{class-name}/{method-name}",
        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    )
}
