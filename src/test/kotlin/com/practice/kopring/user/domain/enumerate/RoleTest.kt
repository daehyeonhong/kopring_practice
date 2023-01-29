package com.practice.kopring.user.domain.enumerate

import com.practice.kopring.exception.InvalidUserRoleException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RoleTest {
    @ParameterizedTest
    @CsvSource(
        value = [
            "ROLE_GUEST, 손님, GUEST",
            "ROLE_USER, 일반 사용자, USER",
        ]
    )
    fun roleTest(input: String, title: String, role: Role): Unit {
        val actual: Role = Role.of(input)
        assertEquals(role, actual)
        assertEquals(title, actual.title)
    }

    @Test
    @DisplayName(value = "assertRole")
    fun assertRole_User(): Unit {
        val input: String = "ROLE_USER"
        assertEquals(Role.of(input), Role.USER)
    }

    @Test
    @DisplayName(value = "assertRole")
    fun assertRole_Guest(): Unit {
        val input: String = "ROLE_GUEST"
        assertEquals(Role.of(input), Role.GUEST)
    }

    @Test
    @DisplayName(value = "assertRole")
    fun assertRole_None(): Unit {
        val input: String = "ROLE_BOSS"
        Assertions.assertThrows(InvalidUserRoleException::class.java) { Role.of(input) }
    }

    @Test
    @DisplayName(value = "Role_Title_Tests")
    fun roleTitleTests(): Unit {
        assertEquals("손님", Role.GUEST.title)
        assertEquals("일반 사용자", Role.USER.title)
    }
}
