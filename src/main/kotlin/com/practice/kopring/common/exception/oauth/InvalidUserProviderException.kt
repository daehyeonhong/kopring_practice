package com.practice.kopring.common.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class InvalidUserProviderException : BusinessException(ErrorMessage.EMAIL_ALREADY_REGISTERED_WITH_OTHER_SERVICE)
