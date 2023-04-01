package com.practice.kopring.oauth.presentation

import com.practice.kopring.auth.dto.AuthUser
import com.practice.kopring.common.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class OAuth2Controller {
    @GetMapping("/login")
    fun loginWithOAuth2(): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }
}
