package com.practice.kopring.user.application

import com.practice.kopring.user.infrastructure.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException


@SpringBootTest
class CustomOAuth2UserServiceTests(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val customOAuth2UserService: CustomOAuth2UserService,
) {
    @MockBean
    @Qualifier("defaultOAuth2UserService")
    private lateinit var defaultOAuth2UserService: DefaultOAuth2UserService

    @Test
    fun loadUserOfNull() {
        Assertions.assertThrows(OAuth2AuthenticationException::class.java) {
            customOAuth2UserService.loadUser(null)
        }
    }

}
