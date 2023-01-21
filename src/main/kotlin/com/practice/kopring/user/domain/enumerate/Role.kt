package com.practice.kopring.user.domain.enumerate

import java.security.InvalidKeyException
import java.util.Arrays

enum class Role(
    val key: String,
    val title: String
) {
    GUEST(key = "ROLE_GUEST", title = "손님"),
    USER(key = "ROLE_USER", title = "일반 사용자");

    companion object {
        fun of(authority: String?): Role? = Arrays.stream(values())
            .filter { it.key == authority }
            .findAny()
            .orElseThrow { InvalidKeyException() }
    }
}
