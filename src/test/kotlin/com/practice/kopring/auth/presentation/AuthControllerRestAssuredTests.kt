package com.practice.kopring.auth.presentation

import RestAssuredTestBase
import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.enumerate.Role
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

class AuthControllerRestAssuredTests(
    @LocalServerPort private val port: Int,
    @Autowired private val auth0JwtTokenProvider: JwtTokenProvider,
    @Autowired private val userRedisCacheService: UserRedisCacheService,
    @Value("\${user_id.key}") private val userId: String,
) : RestAssuredTestBase(port) {
    @Test
    fun login(): Unit {
        val response = RestAssured.given(spec).log().all()
            .`when`().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            )
    }

    companion object : Logging

    @Test
    fun logout(): Unit {
        val accessToken: String = this.auth0JwtTokenProvider.createAccessToken(this.userId, Role.USER)
        logger.info("${Token.ACCESS_TOKEN.name}: ${accessToken}")

        val response = RestAssured.given(spec).log().all()
            .`when`().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            )
            .header(Token.AUTHORIZATION_HEADER.value, Token.BEARER_PREFIX.value + accessToken)
            .get("/auth/logout")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().response()
        response.prettyPrint()
    }

    @Test
    fun refresh(): Unit {
        val refreshToken: String = this.auth0JwtTokenProvider.createRefreshToken(this.userId)
        this.userRedisCacheService.save(RefreshToken(refreshToken, this.userId), 1000 * 60 * 60 * 24 * 7)
        val response = RestAssured.given(spec).log().all()
            .`when`().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            ).header(Token.REFRESH_TOKEN.value, refreshToken)
            .get("/auth/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().response()
    }

    @Test
    fun redirectCookieTest(): Unit {
        RestAssured.given(spec).log().all()
            .`when`().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            ).get("/oauth2/login")
            .then()
            .statusCode(200)
            .extract().response()
    }
}
