package com.practice.kopring.common.domain.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    @GetMapping(value = ["/"])
    fun index(): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }
}
