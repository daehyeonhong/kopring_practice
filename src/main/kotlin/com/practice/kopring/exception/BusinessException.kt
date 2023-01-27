package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

abstract class BusinessException : RuntimeException {
    val errorMessage: ErrorMessage

    constructor(errorMessage: ErrorMessage) : super(errorMessage.description) {
        this.errorMessage = errorMessage
    }

    constructor(errorMessage: ErrorMessage, reason: String) : super(reason) {
        this.errorMessage = errorMessage
    }

    constructor(reason: String) : super(reason) {
        this.errorMessage = ErrorMessage.CONFLICT_ERROR
    }
}
