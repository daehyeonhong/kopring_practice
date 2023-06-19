package com.practice.kopring.auth.presentation

import com.practice.kopring.auth.application.AuthService
import com.practice.kopring.auth.dto.JwtDto
import com.practice.kopring.auth.dto.JwtTokenResponse
import com.practice.kopring.common.dto.ResponseDto
import com.practice.kopring.common.util.CookieUtils
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/accessToken")
    fun fetchAccessToken(
        @RequestParam("oneTimeToken") oneTimeToken: String,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        val userId: String? = this.authService.findByOneTimeToken(oneTimeToken)
        this.authService.revokeOneTimeToken(oneTimeToken)
        return when {
            userId.isNullOrBlank() -> {
                ResponseDto.noContent()
            }

            else -> {
                val jwtDto: JwtDto = this.authService.createTokenWithUserId(userId)
                response.addHeader(
                    "Set-Cookie",
                    CookieUtils.addCookie("access_token", jwtDto.accessToken, 30_000).toString()
                )
                response.addHeader(
                    "Set-Cookie",
                    CookieUtils.addCookie("refresh_token", jwtDto.refreshToken, 60_000).toString()
                )
                ResponseDto.created(Unit)
            }
        }
    }
}
