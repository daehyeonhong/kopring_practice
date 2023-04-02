package com.practice.kopring.auth.dto

data class JwtTokenResponse(private val accessToken: String, private val refreshToken: String)
