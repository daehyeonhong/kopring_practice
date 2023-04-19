package com.practice.kopring.auth.presentation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
class GoogleOAuth2IntegrationTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `Google OAuth2 sign-up or update user information`() {
        val responseEntity = restTemplate.exchange(
            "/login/oauth2/code/google",
            HttpMethod.GET,
            null,
            String::class.java
        )

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
    }
}
