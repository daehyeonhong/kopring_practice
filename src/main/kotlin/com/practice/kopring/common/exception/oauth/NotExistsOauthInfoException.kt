package com.practice.kopring.common.exception.oauth

import com.practice.kopring.common.enumerate.ErrorMessage
import com.practice.kopring.common.exception.BusinessException

class NotExistsOauthInfoException : BusinessException(ErrorMessage.NOT_EXISTS_OAUTH_INFO)
