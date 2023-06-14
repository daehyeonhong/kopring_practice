package com.practice.kopring.auth.dto

import java.util.*

data class AuthInfo(
    val id: UUID,
    val roles: Set<Any>?
)
