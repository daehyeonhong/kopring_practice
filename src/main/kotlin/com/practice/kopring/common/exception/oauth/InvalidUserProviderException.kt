package com.practice.kopring.common.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class InvalidUserProviderException : BusinessException(ErrorMessage.INVALID_PROVIDER_EMAIL)
