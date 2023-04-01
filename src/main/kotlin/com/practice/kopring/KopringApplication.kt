package com.practice.kopring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class KopringApplication

fun main(args: Array<String>) {
    runApplication<KopringApplication>(*args)
}
