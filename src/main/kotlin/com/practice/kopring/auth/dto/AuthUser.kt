package com.practice.kopring.auth.dto

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.RUNTIME)
@MustBeDocumented
@AuthenticationPrincipal
annotation class AuthUser
