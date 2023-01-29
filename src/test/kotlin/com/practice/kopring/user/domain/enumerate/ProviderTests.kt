package com.practice.kopring.user.domain.enumerate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ProviderTests {
    @ParameterizedTest
    @CsvSource(
        value = [
            "github, NONE",
            "google, GOOGLE",
            "FacEBook, FACEBOOK",
            "kakao, NONE"]
    )
    fun providerTest(input: String, provider: Provider): Unit {
        val actual: Provider = Provider.of(input)
        assertEquals(provider, actual)
    }
}
