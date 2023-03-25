package com.practice.kopring.common.domain.presentation

import RestDocsTestBase
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

@AutoConfigureMockMvc
@AutoConfigureRestDocs
class IndexControllerTests(
    @LocalServerPort private val port: Int
) : RestDocsTestBase(port) {

    @Test
    fun rootTest() {
        given(spec).log().all()
            .`when`().log().all().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            )
            .get("/")
            .then()
            .statusCode(200)
            .extract().response().prettyPrint()
    }

}
