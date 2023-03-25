package com.practice.kopring.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.exception.BusinessException

class NotExistsOauthInfoException : BusinessException(ErrorMessage.NOT_EXISTS_OAUTH_INFO)
