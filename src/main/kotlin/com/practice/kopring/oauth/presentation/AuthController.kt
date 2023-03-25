package com.practice.kopring.oauth.presentation

import com.practice.kopring.oauth.application.AuthService
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: AuthService) {
}
