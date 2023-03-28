package com.practice.kopring.common.domain.presentation

import RestAssuredTestBase
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

class IndexControllerRestAssuredTests(
    @LocalServerPort private val port: Int
) : RestAssuredTestBase(port) {

    @Test
    fun googleOauthTest(): Unit {
        given()

            .get("/login/oauth2/code/google")

        given()
            .`when`()
            .redirects()
            .follow(false)["/login/oauth2/code/google?state=8jxbn5nWA2oa6Ds1BuMjZH_HyjgXQfbZOtXTg0UrPm4%3D&code=4%2F0AVHEtk7nY3BIG2gRs1RRtvjXlEGSJ37z-Tu9uc6uQfoh2ang6PlG_bkh1pJ3vaMQaryt9Q&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&authuser=0&prompt=none"]
            .then()
            .statusCode(302)
            .header("Location", containsString("https://accounts.google.com/o/oauth2/auth"))
    }

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
