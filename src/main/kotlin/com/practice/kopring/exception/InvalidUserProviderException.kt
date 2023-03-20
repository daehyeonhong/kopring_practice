package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

class InvalidUserProviderException : BusinessException(ErrorMessage.INVALID_PROVIDER_EMAIL)
