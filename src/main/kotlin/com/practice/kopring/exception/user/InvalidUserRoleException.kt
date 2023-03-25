package com.practice.kopring.exception.user

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class InvalidUserRoleException : BusinessException(ErrorMessage.INVALID_ROLE)
