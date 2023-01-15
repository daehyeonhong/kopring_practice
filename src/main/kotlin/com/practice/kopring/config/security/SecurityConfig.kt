package com.practice.kopring.config.security

import com.practice.kopring.user.application.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService
) {
    @Bean
    fun formLoginFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            oauth2ResourceServer { jwt {} }
        }
        return http.csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
            .oauth2Login {
                it.userInfoEndpoint().userService(this.customOAuth2UserService)
                it.defaultSuccessUrl("/auth/login")
                it.failureUrl("/fail")
            }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/login").permitAll()
                it.requestMatchers(HttpMethod.GET, "/members").permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }
}
