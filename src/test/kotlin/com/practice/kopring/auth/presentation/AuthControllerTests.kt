package com.practice.kopring.auth.presentation

import RestAssuredTestBase
import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.auth.enumerate.Token
import com.practice.kopring.common.logger
import com.practice.kopring.user.enumerate.Role
import io.restassured.RestAssured
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

class AuthControllerTests(
    @LocalServerPort private val port: Int,
    @Autowired private var jwtTokenProvider: JwtTokenProvider,
    @Value("\${user_id.key}") private val userId: String
) : RestAssuredTestBase(port) {

    @Test
    fun login(): Unit {
        val response = RestAssured.given(spec).log().all()
            .sessionId("JSESSIONID", "371EFC4E7BB0C1A42ADD32AD563BC9DF")
            .`when`().filter(
                RestAssuredRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                )
            )
            .get("/login/oauth2/code/google?state=L5aIov5jlRrHxTiMW6qztiCAOqoYlx4Et4RkQiQOpUY%3D&code=4%2F0AVHEtk5P8dnRefp7pCKmQ7ku8Qn1i_5q5TTWqbuPye6PeUpVF705bIvpXPFT23tOUSSeQw&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&authuser=0&prompt=none")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().response()
        response.prettyPrint()
    }

    @Test
    fun logout(): Unit {
        val accessToken: String = this.jwtTokenProvider.createAccessToken(this.userId, Role.USER)
        logger().info("${Token.ACCESS_TOKEN.name}: {}", accessToken)
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
        val refreshToken: String = this.jwtTokenProvider.createRefreshToken(this.userId)
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
}
