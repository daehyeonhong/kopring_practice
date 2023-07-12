package com.practice.kopring.user.application

import com.practice.kopring.common.exception.user.NotExistsUserException
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.dto.UserDto
import com.practice.kopring.user.infrastructure.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun checkExistEmail(email: String): Boolean = this.userRepository.existsByEmail(email)

    fun findByEmail(email: String): UserEntity =
        this.userRepository.findByEmail(email) ?: throw NotExistsUserException()

    fun findByUserId(userId: String): UserDto.UserResponse =
        UserDto.UserResponse.of(this.userRepository.findByUserId(userId) ?: throw NotExistsUserException())
}
