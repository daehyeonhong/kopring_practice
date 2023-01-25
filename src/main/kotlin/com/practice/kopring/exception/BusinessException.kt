package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

abstract class BusinessException : RuntimeException {
    constructor(errorMessage: ErrorMessage) : super(message = errorMessage.description)
    constructor(errorMessage: ErrorMessage, reason: String) : super(message = reason)
    constructor(reason: String, errorMessage: ErrorMessage = ErrorMessage.CONFLICT_ERROR) : super(reason)
}
