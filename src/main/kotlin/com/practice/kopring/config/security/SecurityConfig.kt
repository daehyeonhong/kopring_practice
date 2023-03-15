package com.practice.kopring.config.security

import com.practice.kopring.auth.application.JwtTokenProvider
import com.practice.kopring.oauth.filter.JwtFilter
import com.practice.kopring.oauth.handler.OAuth2SuccessHandler
import com.practice.kopring.user.application.CustomOAuth2UserService
import com.practice.kopring.user.application.UserRedisCacheService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
            .authorizeHttpRequests {
//                it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                it.requestMatchers("/users/login",).permitAll()
//                it.requestMatchers(HttpMethod.GET, "/users").permitAll()
//                it.anyRequest().hasRole(Role.USER.name)
                it.anyRequest().permitAll()
            }
            .oauth2Login {
                it.userInfoEndpoint().userService(this.customOAuth2UserService)
                it.failureUrl("/users/failure")
                it.successHandler(this.oAuth2SuccessHandler)
            }
            .addFilterBefore(
                JwtFilter(this.jwtTokenProvider, this.userRedisCacheService),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()
    }
}
