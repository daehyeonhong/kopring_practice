package com.practice.kopring.config.security

import com.practice.kopring.oauth.dto.AuthInfo
import com.practice.kopring.oauth.dto.AuthUser
import com.practice.kopring.user.domain.enumerate.Role
import java.util.*
import java.util.stream.Collectors
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
        if (authentication.principal == "anonymousUser") {
            throw RuntimeException("")
        }

        return AuthInfo(
            authentication.principal as UUID,
            roles = this.rolesFromAuthorities(authentication.authorities)
        )
    }

    private fun rolesFromAuthorities(authorities: Collection<GrantedAuthority?>): Set<Any>? {
        return authorities.stream()
            .map<Any> { authority: GrantedAuthority? -> Role.of(authority?.authority) }
            .collect(Collectors.toSet())
    }
}
