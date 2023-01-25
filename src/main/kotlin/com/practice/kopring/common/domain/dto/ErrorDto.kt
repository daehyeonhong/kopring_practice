package com.practice.kopring.common.domain.dto

import java.io.Serializable

data class ErrorDto(
    val message: String,
    val reason: String
) : Serializable
