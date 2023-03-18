package com.practice.kopring.user.dto

import com.practice.kopring.user.domain.enumerate.Provider


data class OAuthAttributes(
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val email: String,
    val name: String,
    val picture: String,
    val provider: String
) {
    companion object {
        fun of(
            provider: String, userNameAttributeName: String, attributes: Map<String, Any>
        ): OAuthAttributes = when (Provider.of(provider)) {
            Provider.GOOGLE -> ofGoogle(userNameAttributeName, attributes)
            Provider.FACEBOOK -> ofFacebook(userNameAttributeName, attributes)
            Provider.NONE -> throw IllegalArgumentException(provider)
        }

        private fun ofFacebook(attributeKey: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                nameAttributeKey = attributeKey,
                name = attributes["name"] as String,
                email = attributes["email"] as String,
                picture = attributes["picture"] as String,
                attributes = attributes,
                provider = Provider.FACEBOOK.name
            )
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                nameAttributeKey = attributeKey,
                name = attributes["name"] as String,
                email = attributes["email"] as String,
                picture = attributes["picture"] as String,
                attributes = attributes,
                provider = Provider.GOOGLE.name
            )
        }
    }

    fun convertToMap(): Map<String, Any> {
        return mapOf<String, Any>(
            "id" to nameAttributeKey,
            "key" to nameAttributeKey,
            "name" to name,
            "email" to email,
            "picture" to picture,
            "provider" to provider
        )
    }
}
