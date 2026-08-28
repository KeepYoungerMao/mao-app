package com.mao.common.entity

/**
 * 全局接口响应统一包装类
 */
data class Response<T>(val code: Int, val message: String = "success", val data: T? = null) {

    companion object {

        fun <T> success(data: T): Response<T> {
            return Response(code = ErrorCode.SUCCESS.code, data = data)
        }

        fun error(code: Int, message: String): Response<Nothing?> {
            return Response(code = code, message = message)
        }

        fun error(errorCode: ErrorCode): Response<Nothing?> {
            return Response(code = errorCode.code, message = errorCode.message)
        }

    }

}