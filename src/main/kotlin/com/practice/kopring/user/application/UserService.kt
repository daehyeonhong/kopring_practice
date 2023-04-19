package com.practice.kopring.user.application

import com.practice.kopring.common.exception.user.NotExistsUserException
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.infrastructure.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun checkExistEmail(email: String): Boolean = this.userRepository.existsByEmail(email)

    fun findByEmail(email: String): UserEntity =
        this.userRepository.findByEmail(email) ?: throw NotExistsUserException()
}
