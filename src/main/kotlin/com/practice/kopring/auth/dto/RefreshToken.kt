package com.practice.kopring.auth.dto

import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "refresh:user_", timeToLive = 3_600)
class RefreshToken(
    val refreshToken: String,
    val userId: String
)
