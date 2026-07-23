package com.mao.ex

import com.mao.entity.ErrorCode

class AppException(val code: ErrorCode, message: String = code.message) : RuntimeException(message)