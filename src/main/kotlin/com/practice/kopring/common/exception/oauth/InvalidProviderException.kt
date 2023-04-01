package com.practice.kopring.common.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class InvalidProviderException(provider: String) : BusinessException(ErrorMessage.INVALID_PROVIDER, provider)
