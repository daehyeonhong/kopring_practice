package com.practice.kopring.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableRedisRepositories
class RedisConfiguration(
    @Value("\${spring.data.redis.host}")
    private val host: String,
    @Value("\${spring.data.redis.port}")
    private val port: Int,
) {
    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val lettuceClientConfiguration: LettuceClientConfiguration = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ZERO)
            .shutdownTimeout(Duration.ZERO)
            .build()
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        val template: RedisTemplate<ByteArray, ByteArray> = RedisTemplate<ByteArray, ByteArray>()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        template.setConnectionFactory(this.lettuceConnectionFactory())
        return template
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val stringRedisTemplate: StringRedisTemplate = StringRedisTemplate()
        stringRedisTemplate.keySerializer = StringRedisSerializer()
        stringRedisTemplate.valueSerializer = StringRedisSerializer()
        stringRedisTemplate.setConnectionFactory(this.lettuceConnectionFactory())
        return stringRedisTemplate
    }
}
