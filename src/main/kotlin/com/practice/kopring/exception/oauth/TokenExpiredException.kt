package com.practice.kopring.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class TokenExpiredException : BusinessException(ErrorMessage.EXPIRED_TOKEN)
