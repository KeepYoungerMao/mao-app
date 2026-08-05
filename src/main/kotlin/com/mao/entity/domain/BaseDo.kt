package com.mao.entity.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

/**
 * 数据库公共字段类
 */
open class BaseDo {
    var deleted: Boolean? = null
    @CreatedBy
    var creator: String? = null
    @CreatedDate
    var createTime: LocalDateTime? = null
    @LastModifiedBy
    var updater: String? = null
    @LastModifiedDate
    var updateTime: LocalDateTime? = null
}