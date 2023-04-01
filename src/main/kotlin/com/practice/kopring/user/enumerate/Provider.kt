package com.practice.kopring.user.enumerate

enum class Provider(
) {
    GOOGLE,
    FACEBOOK,
    GITHUB,
    NONE;

    companion object {
        fun of(description: String?): Provider =
            values().find { it.name.equals(description, true) } ?: NONE
    }
}