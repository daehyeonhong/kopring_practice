package com.practice.kopring.common.enumerate

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
enum class ErrorMessage(
    val status: HttpStatus, val description: String
) {
    CONFLICT_ERROR(status = BAD_REQUEST, description = "예기치 못한 에러가 발생했습니다."),

    NOT_EXIST_USER(status = NOT_FOUND, description = "존재하지 않는 사용자입니다."),
    NOT_VALID_ROLE_ERROR(status = FORBIDDEN, description = "유효하지 않은 권한입니다."),

    EXPIRED_TOKEN(status = UNAUTHORIZED, description = "해당 토큰은 만료된 토큰입니다."),
    NOT_VALIDATE_TOKEN(status = UNAUTHORIZED, description = "유효하지 않은 토큰입니다."),
    NOT_EXISTS_OAUTH_INFO(status = NOT_FOUND, description = "존재하지 않는 OAUTH 계정입니다."),

    NOT_VALID_PROVIDER_ERROR(status = BAD_REQUEST, description = "해당 E-Mail 계정은 다른 서비스를 통해 가입한 상태입니다."),
    MAPPER_JSON_ERROR(status = BAD_REQUEST, description = "mapper error가 발생했습니다.");
}
