package com.practice.kopring.user.infrastructure

import com.practice.kopring.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}
