package com.mao.entity

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

/**
 * 分页数据包装类
 */
data class PageResponse<T>(val pageNum: Int, val pageSize: Int, val total: Long, val records: List<T>) {

    /**
     * 提供一个支持流式转换的 map 工具方法
     */
    fun <R> map(mapper: (T) -> R): PageResponse<R> {
        val mappedList = this.records.map(mapper)
        return PageResponse(this.pageNum, this.pageSize, this.total, mappedList)
    }

}