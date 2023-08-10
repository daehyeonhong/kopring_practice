package com.practice.kopring.user.enumerate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*
import java.util.stream.Stream

class ProviderTest {
    class NotSupportedProviderArgumentsProvider
        : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            listOf("APPLE", "KAKAO", "NAVER").stream().map { Arguments.of(it) }
    }

    @ParameterizedTest
    @EnumSource(
        value = Provider::class,
        names = ["GOOGLE", "FACEBOOK", "GITHUB", "NONE"]
    )
    @DisplayName(value = "Provider.of(provider.name) 테스트")
    fun providerOfNameTest(provider: Provider): Unit {
        val actual: Provider = Provider.of(provider.name)
        assertThat(actual).isEqualTo(provider)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "google,GOOGLE",
            "faCeBook,FACEBOOK",
            "GiTHub,GITHUB",
            "NonE,NONE"
        ]
    )
    @DisplayName(value = "Provider.of(provider.title) with lower case 테스트")
    fun providerOfLowerCaseTest(input: String, key: String) {
        val actual: Provider = Provider.of(input)
        assertThat(actual).isEqualTo(Provider.of(key))
    }

    @ParameterizedTest
    @ArgumentsSource(NotSupportedProviderArgumentsProvider::class)
    @DisplayName(value = "Provider.of(invalid provider.name) return NONE 테스트")
    fun providerOfInvalidNameTest(name: String): Unit {
        val actual: Provider = Provider.of(name)
        assertThat(actual).isEqualTo(Provider.NONE)
    }
}
