package com.practice.kopring.user.application

import com.practice.kopring.auth.dto.RefreshToken
import com.practice.kopring.user.enumerate.UserRedisKey
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service

@Service
class UserRedisCacheService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun getWithToken(token: String?): String? = redisTemplate.opsForValue()[token!!]

    fun getWithUserId(userId: String): String? = redisTemplate.opsForValue()["${UserRedisKey.USER_KEY.value}:${userId}"]

    fun save(refreshToken: RefreshToken, expiredTime: Long) {
        val valueOperations: ValueOperations<String, String> = this.redisTemplate.opsForValue()
        valueOperations.set(refreshToken.refreshToken, refreshToken.userId)
        this.redisTemplate.expire(refreshToken.refreshToken, expiredTime, TimeUnit.MILLISECONDS)
    }

    fun delete(userId: Long) {
        redisTemplate.delete("${UserRedisKey.USER_KEY.value}:${userId}")
    }
}
