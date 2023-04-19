package com.practice.kopring.user.application

import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
import java.util.*
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
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
) {
    companion object logger : Logging

    private val oAuth2User = UserEntity(
        name = "개똥이",
        email = "gaeddong@gaeddong.com",
        picture = "qwrqw/qdqd.qwd.com",
        role = Role.USER,
        provider = Provider.GOOGLE
    )

    @Test
    fun findByIdWithUser() {
        val savedEntity: UserEntity = this.userRepository.save(oAuth2User)
        val id: UUID = savedEntity.id
        val foundEntity: UserEntity = this.userService.findByEmail(email = oAuth2User.email)
        Assertions.assertEquals(foundEntity.id, id)
    }
}
