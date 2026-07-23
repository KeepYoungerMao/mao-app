package com.mao.entity

data class Response<T>(val code: Int, val message: String = "success", val data: T? = null) {

    companion object {

        fun <T> success(data: T): Response<T> {
            return Response(code = 200, data = data)
        }

        fun success(): Response<Nothing?> {
            return Response(code = 200)
        }

        fun error(code: Int, message: String): Response<Nothing?> {
            return Response(code = code, message = message)
        }

        fun error(errorCode: ErrorCode): Response<Nothing?> {
            return Response(code = errorCode.code, message = errorCode.message)
        }

    }

}