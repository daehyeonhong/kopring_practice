package com.practice.kopring.common.dto

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.Serializable

data class ResponseDto<T>(val data: T) : Serializable {
    companion object {
        fun <T> ok(data: T): ResponseEntity<T> = ResponseEntity.ok(data)

        fun <T> created(data: T): ResponseEntity<T> = ResponseEntity
            .status(HttpStatus.CREATED)
            .body(data)

        fun noContent(): ResponseEntity<Unit> = ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()

        fun conflict(): ResponseEntity<Unit> = ResponseEntity
            .status(HttpStatus.CONFLICT)
            .build()
    }
}
