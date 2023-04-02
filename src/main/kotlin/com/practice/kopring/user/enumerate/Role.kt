package com.practice.kopring.user.enumerate

import com.practice.kopring.common.exception.user.InvalidUserRoleException

enum class Role(
    val key: String,
    val title: String
) {
    GUEST(key = "ROLE_GUEST", title = "손님"),
    USER(key = "ROLE_USER", title = "일반 사용자");

    companion object {
        fun of(authority: String?): Role = values().find { it.key == authority } ?: throw InvalidUserRoleException()
    }
}
