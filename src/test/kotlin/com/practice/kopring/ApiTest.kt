package com.practice.kopring

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest(
) {
    @LocalServerPort
    private var port = 0

    @BeforeEach
    fun setUp() {
        println(port)
        if (RestAssured.port === RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
        }
    }
}
