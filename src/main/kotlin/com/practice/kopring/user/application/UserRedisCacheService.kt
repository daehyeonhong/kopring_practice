package com.practice.kopring.user.application

import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.user.enumerate.UserRedisKey
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserRedisCacheService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun getWithToken(token: String?): String? = redisTemplate.opsForValue()[token!!]

    fun getWithUserId(userId: String): String? = redisTemplate.opsForValue()["${UserRedisKey.USER_KEY.value}:${userId}"]

    fun save(refreshToken: RefreshToken, expiredTime: Long) {
        redisTemplate.opsForValue()[UserRedisKey.USER_KEY.value + ":" + refreshToken.userId, refreshToken.refreshToken, expiredTime] =
            TimeUnit.MILLISECONDS
    }

    fun delete(userId: String) {
        redisTemplate.delete("${UserRedisKey.USER_KEY.value}:${userId}")
    }
}
