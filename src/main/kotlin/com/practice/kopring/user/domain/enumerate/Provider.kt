package com.practice.kopring.user.domain.enumerate

enum class Provider(
) {
    GOOGLE,
    FACEBOOK,
    NONE;

    companion object {
        fun of(description: String?): Provider =
            values().find { it.name.equals(description, true) } ?: NONE
    }
}
