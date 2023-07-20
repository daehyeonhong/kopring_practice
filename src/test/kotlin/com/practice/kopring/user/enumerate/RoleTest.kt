package com.practice.kopring.user.enumerate

import com.practice.kopring.common.exception.user.InvalidUserRoleException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*
import java.util.stream.Stream


class RoleTest {
    class InvalidRoleArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            listOf("MANAGER", "ADMIN", "CEO").stream().map { Arguments.of(it) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "ROLE_GUEST, 손님, GUEST",
            "ROLE_USER, 일반 사용자, USER",
        ]
    )
    @DisplayName(value = "Role.of() 테스트")
    fun roleOfTest(input: String, title: String, role: Role): Unit {
        val actual: Role = Role.of(input)
        assertThat(actual).isEqualTo(role)
        assertThat(actual.title).isEqualTo(title)
    }

    @ParameterizedTest
    @EnumSource(
        value = Role::class,
        names = ["GUEST", "USER"]
    )
    @DisplayName(value = "Role.of(role.key) 테스트")
    fun roleOfKeyTest(role: Role): Unit {
        val actual: Role = Role.of(role.key)
        assertThat(actual).isEqualTo(role)
        assertThat(actual.title).isEqualTo(role.title)
    }


    @ParameterizedTest
    @ArgumentsSource(InvalidRoleArgumentsProvider::class)
    @DisplayName(value = "Role.of(invalid key) throws InvalidUserRoleException 테스트")
    fun roleOfInvalidKeyTest(key: String): Unit {
        Assertions.assertThatThrownBy {
            Role.of(key)
        }.isInstanceOf(InvalidUserRoleException::class.java)
    }

}
