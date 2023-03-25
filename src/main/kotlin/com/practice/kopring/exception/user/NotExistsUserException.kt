package com.practice.kopring.exception.user

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class NotExistsUserException : BusinessException(ErrorMessage.NOT_EXIST_USER)
