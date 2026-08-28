package com.mao.common.ex

import com.mao.common.entity.ErrorCode

class AppException(val code: ErrorCode, message: String = code.message) : RuntimeException(message)