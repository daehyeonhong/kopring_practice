package com.practice.kopring.exception.handler

import com.practice.kopring.common.dto.ErrorDto
import com.practice.kopring.exception.BusinessException
import jakarta.servlet.http.HttpServletRequest
import java.util.*
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [BusinessException::class])
    protected fun handlerBusinessException(
        exception: BusinessException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorMessage = exception.errorMessage
        return ResponseEntity
            .status(errorMessage.status)
            .body(
                ErrorDto(
                    errorMessage.name,
                    errorMessage.description
                )
            )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    protected fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(BAD_REQUEST)
            .body(
                ErrorDto(
                    exception.javaClass.name,
                    exception.message
                )
            )
    }

    @ExceptionHandler(value = [RequestRejectedException::class])
    protected fun handleRequestRejectedException(
        exception: RequestRejectedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorMessage: String =
            "${exception.cause.toString()}\n${exception.localizedMessage}${Arrays.toString(exception.stackTrace)}"
        return ResponseEntity
            .status(BAD_REQUEST)
            .body(
                ErrorDto(
                    exception.message as String,
                    errorMessage
                )
            )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleException(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(BAD_REQUEST)
            .body(
                ErrorDto(
                    exception.message as String,
                    "${exception.cause.toString()}\n${exception.localizedMessage}${Arrays.toString(exception.stackTrace)}"
                )
            )
    }
}
