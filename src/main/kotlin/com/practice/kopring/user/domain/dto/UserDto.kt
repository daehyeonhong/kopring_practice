package com.practice.kopring.user.domain.dto

class UserDto {
    data class OAuthAttributes(
        val attributes: Map<String, Any>,
        val nameAttributeKey: String,
        val email: String,
        val name: String,
        val picture: String
    ) {
        companion object {
            fun of(
                provider: String,
                userNameAttributeName: String,
                attributes: Map<String, Any>
            ): OAuthAttributes {
                return when (provider) {
                    "google" -> ofGoogle(userNameAttributeName, attributes)
                    else -> throw IllegalArgumentException()
                }
            }

            private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuthAttributes {
                return OAuthAttributes(
                    nameAttributeKey = attributeKey,
                    name = attributes["name"] as String,
                    email = attributes["email"] as String,
                    picture = attributes["picture"] as String,
                    attributes = attributes
                )
            }
        }

        fun convertToMap(): Map<String, Any> {
            return mapOf<String, Any>(
                "id" to nameAttributeKey,
                "key" to nameAttributeKey,
                "name" to name,
                "email" to email,
                "picture" to picture
            )
        }
    }
}
