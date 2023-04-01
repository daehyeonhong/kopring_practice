package com.practice.kopring.common.exception.user

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class NotExistsUserException : BusinessException(ErrorMessage.NOT_EXIST_USER)
