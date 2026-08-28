package com.mao.common.entity

/**
 * 分页查询参数
 */
open class PageQo {
    var pageNum: Int = 1
    var pageSize: Int = 10
    var recount: Boolean = true
    var lastHitCount: Long? = null
    fun offset(): Int = (pageNum - 1) * pageSize
}