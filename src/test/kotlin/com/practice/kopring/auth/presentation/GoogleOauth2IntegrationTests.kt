package com.practice.kopring.auth.presentation

import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("oauth2,database,security,application")
class GoogleOauth2IntegrationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    @WithMockUser
    fun `Google OAuth2 sign-up or update user information`() {
        // Set up existing user or new user
        val email = "test@example.com"
        val userEntity = UserEntity(
            name = "test",
            email = email,
            picture = "test_picture",
            role = Role.USER,
            provider = Provider.GOOGLE
        )
        Mockito.`when`(userRepository.findByEmail(email)).thenReturn(userEntity)

        // Perform GET request
        mockMvc.perform(
            get("/login/oauth2/code/google")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        // Verify the userRepository interactions
        Mockito.verify(userRepository).findByEmail(email)
        Mockito.verify(userRepository).save(Mockito.any(UserEntity::class.java))
    }
}
