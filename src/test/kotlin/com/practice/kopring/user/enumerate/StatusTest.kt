package com.practice.kopring.user.enumerate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class StatusTest {
    @ParameterizedTest
    @CsvSource(
        value = [
            "true,SUCCESS,로그인 성공",
            "false,SIGNUP,회원가입"
        ]
    )
    fun statusOfTest(check: Boolean, status: Status, description: String) {
        val actual: Status = Status.of(check)
        assertThat(actual).isEqualTo(status)
        assertThat(actual.description).isEqualTo(description)
    }

    @Test
    fun statusExceptionTest() {
        assertThat(Status.entries.size).isEqualTo(2)
    }
}
