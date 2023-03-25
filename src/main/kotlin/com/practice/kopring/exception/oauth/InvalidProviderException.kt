package com.practice.kopring.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class InvalidProviderException(provider: String) : BusinessException(ErrorMessage.INVALID_PROVIDER, provider)
