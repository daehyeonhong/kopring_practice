package com.practice.kopring.user.enumerate

enum class Status(
    val description: String
) {
    SUCCESS(description = "로그인 성공"),
    SIGNUP(description = "회원가입");

    companion object {
        fun of(check: Boolean): Status = if (check) SUCCESS else SIGNUP
    }
}
