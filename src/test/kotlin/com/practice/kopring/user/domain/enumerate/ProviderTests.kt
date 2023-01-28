package com.practice.kopring.user.domain.enumerate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProviderTests {
    @Test
    @DisplayName(value = "facebook Provider Test")
    fun facebookTest(): Unit {
        val input: String = "FaCeBook"
        assertEquals(Provider.of(input), Provider.FACEBOOK)
    }

    @Test
    @DisplayName(value = "잘못된값_1")
    fun noneTest(): Unit {
        val input: String = "github"
        assertEquals(Provider.of(input), Provider.NONE)
    }

    @Test
    @DisplayName(value = "잘못된값_2")
    fun nullTest(): Unit {
        val input: String? = null
        assertEquals(Provider.of(input), Provider.NONE)
    }

    @Test
    @DisplayName(value = "Google Provider Test")
    fun googleTest(): Unit {
        val google: String = "GooGLE"
        assertEquals(Provider.of(google), Provider.GOOGLE)
    }
}
