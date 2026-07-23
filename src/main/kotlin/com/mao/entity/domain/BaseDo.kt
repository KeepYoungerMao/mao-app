package com.mao.entity.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate

/**
 * 数据库公共字段类
 */
open class BaseDo {
    @CreatedBy
    var creator: String? = null
    @CreatedDate
    var createTime: Long? = null
    @LastModifiedBy
    var updater: String? = null
    @LastModifiedDate
    var updateTime: Long? = null
}