package com.practice.kopring.auth.presentation

import com.practice.kopring.auth.application.AuthService
import com.practice.kopring.auth.dto.JwtTokenResponse
import com.practice.kopring.common.dto.ResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @GetMapping("/refresh")
    fun refreshToken(
        @RequestHeader(value = "refresh_token") refreshToken: String?
    ): ResponseEntity<JwtTokenResponse> = ResponseDto.ok(this.authService.refresh(refreshToken))

    @GetMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<Unit> {
        this.authService.revokeToken(token)
        return ResponseDto.noContent()
    }
}
