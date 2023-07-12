package com.practice.kopring.user.dto

import com.practice.kopring.user.domain.UserEntity

class UserDto {
    data class UserResponse(
        val name: String,
        val email: String,
        val picture: String
    ) {
        companion object {
            fun of(user: UserEntity): UserResponse =
                UserResponse(name = user.name, email = user.email, picture = user.picture)
        }
    }
}
