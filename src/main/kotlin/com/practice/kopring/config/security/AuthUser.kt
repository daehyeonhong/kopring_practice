package com.practice.kopring.config.security

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class AuthUser
