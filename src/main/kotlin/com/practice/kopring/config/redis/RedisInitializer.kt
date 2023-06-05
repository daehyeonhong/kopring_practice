package com.practice.kopring.config.redis

import jakarta.annotation.PostConstruct
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory

@Configuration
class RedisInitializer(
    private val redisConnectionFactory: RedisConnectionFactory
) {
    companion object : Logging

    @PostConstruct
    fun checkRedisConnection() {
        try {
            val connectionTestResult: String? = this.redisConnectionFactory.connection.ping()
            if (connectionTestResult != null) logger.info("Redis connection is OK: PING-${connectionTestResult}")
        } catch (e: Exception) {
            throw RuntimeException("Cannot connect to Redis", e)
        }
    }
}
