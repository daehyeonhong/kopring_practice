package com.practice.kopring.config.security

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.oauth.filter.JwtFilter
import com.practice.kopring.oauth.handler.OAuth2SuccessHandler
import com.practice.kopring.user.application.CustomOAuth2UserService
import com.practice.kopring.user.application.UserRedisCacheService
import com.practice.kopring.user.domain.enumerate.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsUtils

@Configuration
@EnableWebSecurity
class SecurityConfig constructor(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val userRedisCacheService: UserRedisCacheService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun formLoginFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
            .oauth2Login {
                it.userInfoEndpoint().userService(this.customOAuth2UserService)
                it.defaultSuccessUrl("/user/signIn")
                it.failureUrl("/fail")
                it.successHandler(this.oAuth2SuccessHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                it.requestMatchers("/auth/login").permitAll()
                it.requestMatchers(HttpMethod.GET, "/users").permitAll()
                it.anyRequest().hasRole(Role.USER.name)
            }
            .addFilterBefore(
                JwtFilter(this.jwtTokenProvider, this.userRedisCacheService),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()
    }
}
