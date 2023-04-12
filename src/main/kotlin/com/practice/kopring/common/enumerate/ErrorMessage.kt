package com.practice.kopring.common.enumerate

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED

enum class ErrorMessage(
    val status: HttpStatus, val description: String
) {
    /**
     * Common Error Code
     */
    CONFLICT_ERROR(status = BAD_REQUEST, description = "예기치 못한 에러가 발생했습니다."),
    MAPPER_JSON(status = BAD_REQUEST, description = "mapper error가 발생했습니다."),

    /**
     * User Error Code
     */
    NOT_EXIST_USER(status = NOT_FOUND, description = "존재하지 않는 사용자입니다."),
    INVALID_ROLE(status = FORBIDDEN, description = "유효하지 않은 권한입니다."),

    /**
     * OAuth Error Code
     */
    NOT_EXISTS_OAUTH_INFO(status = NOT_FOUND, description = "존재하지 않는 OAUTH 계정입니다."),
    TOKEN_EXPIRED(status = UNAUTHORIZED, description = "해당 토큰은 만료된 토큰입니다."),
    TOKEN_INVALID(status = UNAUTHORIZED, description = "유효하지 않은 토큰입니다."),
    TOKEN_PROCESS_EXCEPTION(status = UNAUTHORIZED, description = "유효하지 않은 토큰입니다."),
    INVALID_PROVIDER_EMAIL(status = CONFLICT, description = "해당 E-Mail 계정은 다른 서비스를 통해 가입한 상태입니다."),
    INVALID_PROVIDER(status = BAD_REQUEST, description = "지원하지 않는 OAuth2 서비스 제공자입니다."),
}
