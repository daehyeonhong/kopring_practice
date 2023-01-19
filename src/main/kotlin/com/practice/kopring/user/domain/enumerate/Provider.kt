package com.practice.kopring.user.domain.enumerate

import java.util.*

enum class Provider(
    val description: String
) {
    GOOGLE(description = "google"),
    NONE(description = "none");

    companion object {
        fun of(description: String?): Provider =
            Arrays.stream(values())
                .filter { it.description == description }
                .findAny()
                .orElse(NONE)
    }
}
