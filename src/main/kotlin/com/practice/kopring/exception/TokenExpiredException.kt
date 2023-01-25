package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

class TokenExpiredException : BusinessException(ErrorMessage.EXPIRED_TOKEN)
