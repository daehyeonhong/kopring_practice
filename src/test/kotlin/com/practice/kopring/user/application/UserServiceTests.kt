package com.practice.kopring.user.application

import com.practice.kopring.common.exception.user.NotExistsUserException
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceException
import java.util.*
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class UserServiceTests(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val entityManager: EntityManager,
) {
    companion object logger : Logging

    @Test
    fun findByEmailWithUser() {
        val gaeddongs: UserEntity = UserEntity(
            name = "개똥이",
            email = "gaeddong@gaeddong.com",
            picture = "qwrqw/qdqd.qwd.com",
            role = Role.USER,
            provider = Provider.GOOGLE
        )
        val savedEntity: UserEntity = this.userRepository.save(gaeddongs)
        val id: UUID = savedEntity.id
        val foundEntity: UserEntity = this.userService.findByEmail(email = gaeddongs.email)
        Assertions.assertEquals(foundEntity.id, id)
    }

    @Test
    fun findByEmailWithoutUser() {
        assertThrows<NotExistsUserException> {
            this.userService.findByEmail(email = UUID.randomUUID().toString())
        }
    }

    @Test
    fun checkExistEmailWithUser() {
        val gaeddongs: UserEntity = UserEntity(
            name = "개똥이",
            email = "gaeddong@gaeddong.com",
            picture = "qwrqw/qdqd.qwd.com",
            role = Role.USER,
            provider = Provider.GOOGLE
        )
        val savedEntity: UserEntity = this.userRepository.save(gaeddongs)
        val id: UUID = savedEntity.id
        val isExist: Boolean = this.userService.checkExistEmail(email = gaeddongs.email)
        Assertions.assertTrue(isExist)
    }

    @Test
    fun checkExistEmailWithoutUser() {
        val isExist: Boolean = this.userService.checkExistEmail(email = "김개똥")
        Assertions.assertFalse(isExist)
    }

    @Test
    fun emailConflict(): Unit {
        val soddongs: UserEntity = UserEntity(
            name = "소똥이",
            email = "soddong@gaeddong.com",
            picture = "qwrqw/qdqd.qwd.com",
            role = Role.USER,
            provider = Provider.GOOGLE
        )

        val souddongs: UserEntity = UserEntity(
            name = "소우똥이",
            email = "soddong@gaeddong.com",
            picture = "qwrqw/qdqd.qwd.com",
            role = Role.USER,
            provider = Provider.GOOGLE
        )

        this.userRepository.save(soddongs)
        entityManager.flush()
        this.userRepository.save(souddongs)
        assertThrows<PersistenceException> { entityManager.flush() }
    }
}
