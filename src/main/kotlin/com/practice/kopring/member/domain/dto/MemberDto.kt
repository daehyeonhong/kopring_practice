package com.practice.kopring.member.domain.dto

import com.practice.kopring.member.domain.entity.MemberEntity
import com.practice.kopring.member.domain.enumerate.Role
import java.io.Serializable

class MemberDto {
    data class OAuthAttributes(
        val attributes: Map<String, Any>,
        val nameAttributeKey: String,
        val name: String,
        val email: String,
        val picture: String
    ) {
        companion object {
            fun of(
                registrationId: String,
                userNameAttributeName: String,
                attributes: Map<String, Any>
            ): OAuthAttributes {
                return OAuthAttributes(
                    name = attributes["name"] as String,
                    email = attributes["email"] as String,
                    picture = attributes["picture"] as String,
                    attributes = attributes,
                    nameAttributeKey = userNameAttributeName
                )
            }
        }
    }

    data class SessionUser(
        private val member: MemberEntity
    ) : Serializable {
        val name = member.name
        val email = member.email
        val picture = member.picture
    }
}

fun MemberDto.OAuthAttributes.toEntity(): MemberEntity {
    return MemberEntity(
        name = name,
        email = email,
        picture = picture,
        role = Role.USER
    )
}
