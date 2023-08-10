package com.practice.kopring.common.exception.handler

import com.practice.kopring.common.dto.ErrorDto
import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException
import com.practice.kopring.common.exception.DefaultBusinessException
import com.practice.kopring.common.exception.auth.TokenProcessException
import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object : Logging

    @ExceptionHandler(value = [BusinessException::class])
    protected fun handlerBusinessException(
        exception: BusinessException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorDto> {
        logger.error {
            exception.javaClass.name
            exception
        }
        val errorMessage: ErrorMessage = exception.errorMessage
        return ResponseEntity.status(errorMessage.status).body(
            ErrorDto(
                errorMessage.name, errorMessage.description
            )
        )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    protected fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorDto> {
        logger.error {
            exception.javaClass.name
            exception.bindingResult
            "requestUrl: ${request.requestURL}"
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorDto(
                "MethodArgumentNotValidException", exception.message
            )
        )
    }

    @ExceptionHandler(value = [RequestRejectedException::class])
    protected fun handleRequestRejectedException(
        exception: RequestRejectedException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorDto(
                    exception.javaClass.name,
                    exception.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleException(
        exception: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorDto> {
        logger.error {
            exception.javaClass.name
            exception
        }
        val errorMessage: ErrorMessage = DefaultBusinessException().errorMessage
        return ResponseEntity.status(errorMessage.status).body(
            ErrorDto(
                errorMessage.name,
                errorMessage.description,
            )
        )
    }

    @ExceptionHandler(value = [JwtException::class])
    protected fun handleJwtException(
        exception: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorDto> {
        logger.error {
            exception.javaClass.name
            exception
        }
        val errorMessage: ErrorMessage = TokenProcessException().errorMessage
        return ResponseEntity.status(errorMessage.status).body(
            ErrorDto(
                errorMessage.name,
                errorMessage.description
            )
        )
    }
}
