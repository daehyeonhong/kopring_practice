package com.practice.kopring.config.security

import java.util.*

data class AuthInfo(
    val id: UUID,
    val roles: MutableList<Any>?
)
