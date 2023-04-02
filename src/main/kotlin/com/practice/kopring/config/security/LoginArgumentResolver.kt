package com.practice.kopring.config.security

import com.practice.kopring.auth.dto.AuthInfo
import com.practice.kopring.auth.dto.AuthUser
import com.practice.kopring.common.exception.oauth.NotExistsOauthInfoException
import com.practice.kopring.user.enumerate.Role
import java.util.*
import org.springframework.core.MethodParameter
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isLoginUserAnnotation: Boolean = parameter.getParameterAnnotation(AuthUser::class.java) != null
        val isAuthInfo: Boolean = parameter.parameterType == AuthInfo::class.java

        return isLoginUserAnnotation && isAuthInfo
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return when {
            (authentication.principal == "anonymousUser") -> throw NotExistsOauthInfoException()
            else -> AuthInfo(
                id = authentication.principal as UUID,
                roles = this.rolesFromAuthorities(authentication.authorities)
            )
        }
    }

    private fun rolesFromAuthorities(authorities: Collection<GrantedAuthority?>): Set<Any> {
        return setOf(authorities.map { authority: GrantedAuthority? ->
            Role.of(authority?.authority)
        })
    }
}
