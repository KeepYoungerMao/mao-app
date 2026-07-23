package com.mao.entity.query

/// 分页数据查询类
open class PageQo {
    var pageNum: Int = 1
    var pageSize: Int = 10
    var recount: Boolean = false
    var lastHitCount: Long? = null

    fun offset(): Int = (pageNum - 1) * pageSize
}