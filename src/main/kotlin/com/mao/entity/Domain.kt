package com.mao.entity

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
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

@Table("sys_user")
data class UserDo(
    @Id
    var id: Int? = null,
    var username: String? = null,
    var password: String? = null,
    var avatar: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var expired: Boolean? = null,
    var locked: Boolean? = null,
    var enabled: Boolean? = null,
    var expireTime: LocalDateTime? = null,
    var lastLoginTime: LocalDateTime? = null,
    var mustChangePassword: Boolean? = null,
) : BaseDo()

/**
 * 角色
 */
@Table("sys_role")
data class RoleDo(
    @Id
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
) : BaseDo()

/**
 * 系统操作日志
 */
@Table("sys_operation_log")
data class OperationLogDo(
    var id: Long? = null,
    var username: String? = null,
    var scope: String? = null,
    var module: String? = null,
    var operation: String? = null,
    var description: String? = null,
    var method: String? = null,
    var ip: String? = null,
    var success: Boolean? = null,
    var errorMessage: String? = null,
    var operationTime: LocalDateTime? = null,
    var cost: Long? = null
)