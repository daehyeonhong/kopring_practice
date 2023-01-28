package com.practice.kopring.user.domain.enumerate

import com.practice.kopring.exception.InvalidUserRoleException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RoleTest {
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
}
