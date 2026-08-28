package com.mao.common.entity

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
