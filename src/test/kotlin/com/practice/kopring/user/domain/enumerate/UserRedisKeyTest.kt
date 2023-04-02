package com.practice.kopring.user.domain.enumerate

import com.practice.kopring.user.enumerate.UserRedisKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserRedisKeyTest {
    @ParameterizedTest
    @CsvSource(
        value = [
            "refresh:user_, USER_KEY"
        ]
    )
    fun redisKeyTest(input: String, userRedisKey: UserRedisKey): Unit {
        assertEquals(input, userRedisKey.value)
    }

    @Test
    @DisplayName(value = "redisKey Test")
    fun redisKeyTest(): Unit {
        assertEquals(UserRedisKey.USER_KEY.value, "refresh:user_")
    }
}
