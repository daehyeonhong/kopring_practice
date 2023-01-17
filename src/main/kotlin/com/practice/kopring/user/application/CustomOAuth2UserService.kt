package com.practice.kopring.user.application

import com.practice.kopring.user.domain.dto.UserDto.OAuthAttributes
import com.practice.kopring.user.domain.dto.toEntity
import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.infrastructure.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service


@Service
class CustomOAuth2UserService(
    private val httpSession: HttpSession,
    private val userRepository: UserRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        userRequest ?: throw OAuth2AuthenticationException("Error")
        val delegate: DefaultOAuth2UserService = DefaultOAuth2UserService()
        val oAuth2User: OAuth2User = delegate.loadUser(userRequest)

        val registrationId: String = userRequest.clientRegistration.registrationId
        val userNameAttributeName: String =
            userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val attributes: OAuthAttributes = OAuthAttributes.of(
            registrationId,
            userNameAttributeName,
            oAuth2User.attributes
        )
        val userEntity: UserEntity = this.saveOrUpdate(attributes)
        httpSession.setAttribute("member", userEntity)

        return DefaultOAuth2User(
            setOf(SimpleGrantedAuthority(userEntity.role.key)),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): UserEntity {
        val userEntity: UserEntity = this.userRepository.findByEmail(attributes.email)
            ?.apply { this.loginUpdate(name, picture) }
            ?: attributes.toEntity()
        return this.userRepository.save(userEntity)
    }
}
