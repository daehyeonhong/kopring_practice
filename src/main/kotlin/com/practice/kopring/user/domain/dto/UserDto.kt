package com.practice.kopring.user.domain.dto

import com.practice.kopring.user.domain.entity.UserEntity
import com.practice.kopring.user.domain.enumerate.Role
import java.io.Serializable

class UserDto {
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
        private val member: UserEntity
    ) : Serializable {
        val name = member.name
        val email = member.email
        val picture = member.picture
    }
}

fun UserDto.OAuthAttributes.toEntity(): UserEntity {
    return UserEntity(
        name = name,
        email = email,
        picture = picture,
        role = Role.USER
    )
}
