package com.practice.kopring.common.domain.presentation

import com.practice.kopring.ApiTest
import com.practice.kopring.auth.application.JwtTokenProvider
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class IndexControllerTests(
    @Autowired
    private val jwtTokenProvider: JwtTokenProvider
) : ApiTest() {

    @Test
    fun `OAuth2를 이용한 회원가입 테스트`() {
        given()
            .`when`()
            .get("/")
            .then()
            .statusCode(302)
            .contentType(ContentType.HTML)
            .header("Location", containsString("https://accounts.google.com/o/oauth2/v2"))
    }

}
