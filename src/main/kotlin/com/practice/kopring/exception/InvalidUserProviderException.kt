package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

class InvalidUserProviderException : BusinessException(ErrorMessage.NOT_VALID_PROVIDER_ERROR)
