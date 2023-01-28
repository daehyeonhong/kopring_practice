package com.practice.kopring.oauth.dto

import java.util.*

data class AuthInfo(
    val id: UUID,
    val roles: Set<Any>?
)
