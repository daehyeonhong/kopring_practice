package com.practice.kopring.common.enumerate

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

enum class ErrorMessage(
    val status: HttpStatus, val description: String,
) {
    /**
     * Common Error Code
     */
    INTERNAL_SERVER_ERROR(status = BAD_REQUEST, description = "예기치 못한 에러가 발생했습니다."),
    INVALID_MAPPING(status = BAD_REQUEST, description = "mapper error가 발생했습니다."),

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
    TOKEN_PROCESS_EXCEPTION(status = UNAUTHORIZED, description = "토큰 유효성 검증 중 예기치 못한 에러가 발생했습니다."),
    EMAIL_ALREADY_REGISTERED_WITH_OTHER_SERVICE(
        status = CONFLICT,
        description = "다른 서비스에서 사용하는 이메일 계정으로 가입한 경우, 해당 서비스에서 로그인하여 이메일 계정을 인증해주세요."
    ),
    UNSUPPORTED_OAUTH2_PROVIDER(status = BAD_REQUEST, description = "지원하지 않는 OAuth2 서비스 제공자입니다."),
}
