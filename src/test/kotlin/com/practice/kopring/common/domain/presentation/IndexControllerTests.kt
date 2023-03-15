package com.practice.kopring.common.domain.presentation

import com.practice.kopring.ApiTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test

class IndexControllerTests(
) : ApiTest() {

    @Test
    fun `OAuth2를 이용한 회원가입 테스트`() {
        given().log().all()
            .`when`().log().all()
            .get("/")
            .then()
            .statusCode(200)
    }

}
