package com.practice.kopring.user.enumerate

enum class Provider {
    GOOGLE,
    FACEBOOK,
    GITHUB,
    NONE;

    companion object {
        fun of(provider: String?): Provider =
            entries.find { it.name.equals(provider, true) } ?: NONE
    }
}
