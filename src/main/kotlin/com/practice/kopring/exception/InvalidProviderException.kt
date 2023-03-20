package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

class InvalidProviderException(provider: String) : BusinessException(ErrorMessage.INVALID_PROVIDER, provider)
