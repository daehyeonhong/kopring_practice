package com.practice.kopring.common.domain.presentation

import RestAssuredTestBase
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

class IndexControllerRestAssuredTests(
    @LocalServerPort private val port: Int
) : RestAssuredTestBase(port) {

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
