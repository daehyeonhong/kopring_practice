package com.practice.kopring.exception

import com.practice.kopring.common.enumerate.ErrorMessage

class InvalidUserRoleException : BusinessException(ErrorMessage.NOT_VALID_ROLE_ERROR)
