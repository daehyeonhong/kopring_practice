package com.practice.kopring.auth.enumerate

enum class Token(val value: String) {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token"),
    BEARER_PREFIX("Bearer "),
    AUTHORIZATION_HEADER("Authorization"),
}
