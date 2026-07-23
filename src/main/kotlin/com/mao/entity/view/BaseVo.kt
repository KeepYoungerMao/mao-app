package com.mao.entity.view

import com.mao.extension.TimestampToStrSerializer
import tools.jackson.databind.annotation.JsonSerialize

/**
 * 查询结果数据公共字段类
 */
open class BaseVo {

    var creator: String? = null

    @JsonSerialize(using = TimestampToStrSerializer::class)
    var createTime: Long? = null

    var updater: String? = null

    @JsonSerialize(using = TimestampToStrSerializer::class)
    var updateTime: Long? = null

}