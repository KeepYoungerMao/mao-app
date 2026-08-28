package com.mao.common.entity

import java.time.LocalDateTime

/**
 * 查询结果数据公共字段类
 */
open class BaseVo {
    var creator: String? = null
    var createTime: LocalDateTime? = null
    var updater: String? = null
    var updateTime: LocalDateTime? = null
}