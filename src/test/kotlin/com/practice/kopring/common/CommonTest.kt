package com.practice.kopring.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import org.junit.jupiter.api.Test

class CommonTest {
    @Test
    fun currentMillsTest(): Unit {
        val start: Long = System.currentTimeMillis()
        for (i in 0..500000) {
            Date(System.currentTimeMillis())
            Date(System.currentTimeMillis() + 2 * 60 * 60 * 1_000)
        }
        val end: Long = System.currentTimeMillis()
        println("time: ${end - start}")
    }

    @Test
    fun localDateTimeTest(): Unit {
        val start: Long = System.currentTimeMillis()
        for (i in 0..500000) {
            Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant())
            Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.of("Asia/Seoul")).toInstant())
        }
        val end: Long = System.currentTimeMillis()
        println("time: ${end - start}")
    }
}
