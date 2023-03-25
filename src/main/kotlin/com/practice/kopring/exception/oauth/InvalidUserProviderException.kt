package com.practice.kopring.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class InvalidUserProviderException : BusinessException(ErrorMessage.INVALID_PROVIDER_EMAIL)
