package com.practice.kopring.common.exception.auth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class TokenInvalidException : BusinessException(ErrorMessage.INVALID_TOKEN)
