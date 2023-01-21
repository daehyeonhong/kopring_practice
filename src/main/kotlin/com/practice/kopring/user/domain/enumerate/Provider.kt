package com.practice.kopring.user.domain.enumerate

import java.util.Arrays

enum class Provider(
) {
    GOOGLE,
    FACEBOOK,
    NONE;

    companion object {
        fun of(description: String?): Provider =
            Arrays.stream(values())
                .filter { it.name.equals(description, true) }
                .findAny()
                .orElse(NONE)
    }
}
