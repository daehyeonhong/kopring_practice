package com.practice.kopring.oauth.config

import java.util.UUID

data class AuthInfo(
    val id: UUID,
    val roles: MutableList<Any>?
)
