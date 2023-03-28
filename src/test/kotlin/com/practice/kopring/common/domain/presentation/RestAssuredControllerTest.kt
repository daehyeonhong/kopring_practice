package com.practice.kopring.common.domain.presentation

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestAssuredControllerTest {
    @Test
    fun restAssuredTest() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080
        RestAssured.given()
            .`when`()
            .get("/login/oauth2/code/google?state=L5aIov5jlRrHxTiMW6qztiCAOqoYlx4Et4RkQiQOpUY%3D&code=4%2F0AVHEtk5P8dnRefp7pCKmQ7ku8Qn1i_5q5TTWqbuPye6PeUpVF705bIvpXPFT23tOUSSeQw&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&authuser=0&prompt=none")
            .prettyPrint()
    }
}
