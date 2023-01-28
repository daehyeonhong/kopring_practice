package com.practice.kopring.user.domain.enumerate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserRedisKeyTest {
    @Test
    @DisplayName(value = "redisKey Test")
    fun redisKeyTest(): Unit {
        assertEquals(UserRedisKey.USER_KEY.value, "refresh:user_")
    }
}
