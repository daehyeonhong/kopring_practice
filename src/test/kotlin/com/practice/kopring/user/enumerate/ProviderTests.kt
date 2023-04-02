package com.practice.kopring.user.enumerate

import com.practice.kopring.user.enumerate.Provider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ProviderTests {
    @ParameterizedTest
    @CsvSource(
        value = [
            "github, GITHUB",
            "google, GOOGLE",
            "FacEBook, FACEBOOK",
            "kakao, NONE"]
    )
    fun providerTest(input: String, provider: Provider): Unit {
        val actual: Provider = Provider.of(input)
        assertEquals(provider, actual)
    }
}