package com.practice.kopring.user.application

import com.practice.kopring.common.exception.oauth.InvalidUserProviderException
import com.practice.kopring.user.domain.UserEntity
import com.practice.kopring.user.dto.OAuthAttributes
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import com.practice.kopring.user.infrastructure.UserRepository
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
    private val userRepository: UserRepository,
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
        return DefaultOAuth2User(
            setOf(SimpleGrantedAuthority(Role.USER.key)),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    fun saveOrUpdate(oauth2User: OAuth2User, provider: Provider): UserEntity {
        val data: Map<String, Any> = oauth2User.attributes
        val email: String = data["email"] as String

        val userEntity: UserEntity? = userRepository.findByEmail(email)
        val name = data["name"] as String
        val picture = data["picture"] as String
        when (userEntity) {
            null -> {
                return this.userRepository.save(
                    UserEntity(
                        name = name,
                        email = email,
                        picture = picture,
                        role = Role.USER,
                        provider = provider
                    )
                )
            }

            else -> {
                if (userEntity.provider === provider) {
                    userEntity.loginUpdate(name, picture)
                    return this.userRepository.save(userEntity)
                }
                throw InvalidUserProviderException()
            }
        }
    }
}
