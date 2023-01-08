package com.practice.kopring.member.application

import com.practice.kopring.member.domain.dto.MemberDto.OAuthAttributes
import com.practice.kopring.member.domain.dto.toEntity
import com.practice.kopring.member.domain.entity.MemberEntity
import com.practice.kopring.member.infrastructure.MemberRepository
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
    private val memberRepository: MemberRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        if (userRequest == null) throw OAuth2AuthenticationException("Error")
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
        val memberEntity: MemberEntity = this.saveOrUpdate(attributes)
        httpSession.setAttribute("member", memberEntity)

        return DefaultOAuth2User(
            setOf(SimpleGrantedAuthority(memberEntity.role.key)),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): MemberEntity {
        val memberEntity: MemberEntity = this.memberRepository.findByEmail(attributes.email)
            ?.apply {
                this.name = attributes.name
                this.picture = attributes.picture
            }
            ?: attributes.toEntity()
        return this.memberRepository.save(memberEntity)
    }
}
