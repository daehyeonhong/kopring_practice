package com.practice.kopring.common.exception.user

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class InvalidUserRoleException : BusinessException(ErrorMessage.INVALID_ROLE)
