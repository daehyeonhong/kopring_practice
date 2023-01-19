package com.practice.kopring.user.application

import com.practice.kopring.user.domain.enumerate.UserRedisKey
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserRedisCacheService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun getWithToken(token: String?): String? = redisTemplate.opsForValue()[token!!]

    fun getWithUserId(userId: String): String? = redisTemplate.opsForValue()[UserRedisKey.USER_KEY.value + ":" + userId]

    fun update(key: String, value: String?, expiredTime: Long) {
        redisTemplate.opsForValue()[UserRedisKey.USER_KEY.value + ":" + key, value!!, expiredTime] =
            TimeUnit.MILLISECONDS
    }

    fun delete(userId: Long) {
        redisTemplate.delete(UserRedisKey.USER_KEY.value + ":" + userId)
    }
}
