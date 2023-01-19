package com.practice.kopring.user.application

import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.infrastructure.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun checkExistEmail(email: String): Boolean {
        return this.userRepository.existsByEmail(email)
    }

    fun findByEmail(email: String): UserEntity {
        return this.userRepository.findByEmail(email)!!
    }
}
