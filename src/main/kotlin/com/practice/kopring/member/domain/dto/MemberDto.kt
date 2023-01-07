package com.practice.kopring.member.domain.dto

class MemberDto {
    data class OAuthAttributes(
        val attributes: Map<String, Any>,
        val nameAttributeKey: String,
        val name: String,
        val email: String,
        val picture: String
    ) {
        companion object {
            @JvmStatic
            fun of(
                registrationId: String,
                userNameAttributeName: String,
                attributes: Map<String, Any>
            ): OAuthAttributes {
                return OAuthAttributes(
                    attributes = attributes,
                    nameAttributeKey = userNameAttributeName,
                    name = attributes["name"] as String,
                    email = attributes["email"] as String,
                    picture = attributes["picture"] as String
                )
            }
        }
    }
}
