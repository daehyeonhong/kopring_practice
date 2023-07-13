package com.practice.kopring.user.presentation

import com.practice.kopring.user.application.UserService
import com.practice.kopring.user.dto.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController(private val userService: UserService) {
    @GetMapping(value = ["/users/{userId}"])
    fun findByUserId(@PathVariable userId: String): ResponseEntity<UserDto.UserResponse> =
        ResponseEntity.ok(this.userService.findById(userId))
}
