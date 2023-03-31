package com.practice.kopring.oauth.presentation

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.oauth.application.OAuth2Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class OAuth2Controller(
    private val oAuth2Service: OAuth2Service,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @GetMapping("/login")
    fun loginWithOAuth2(): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }
}
