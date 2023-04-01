package com.practice.kopring.config.time

import jakarta.annotation.PostConstruct
import java.util.*
import org.springframework.context.annotation.Configuration

@Configuration
class TimeZoneConfiguration {
    @PostConstruct
    private fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}
