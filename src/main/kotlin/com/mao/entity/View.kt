package com.mao.entity

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

/**
 * 操作类接口，无数据返回时，返回提示性信息
 */
data class Tips(val message : String, val operationTime: LocalDateTime = LocalDateTime.now())

/**
 * 系统用户
 */
data class UserVo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expired: Boolean? = null,
    val locked: Boolean? = null,
    val enabled: Boolean? = null,
    val expireTime: LocalDateTime? = null,
    val lastLoginTime: LocalDateTime? = null
) : BaseVo()

/**
 * 系统操作日志
 */
data class OperationLogVo(
    val id: Long? = null,
    val username: String? = null,
    val scope: String? = null,
    val module: String? = null,
    val operation: String? = null,
    val description: String? = null,
    val method: String? = null,
    val ip: String? = null,
    val success: Boolean? = null,
    val errorMessage: String? = null,
    val operationTime: LocalDateTime? = null,
    val cost: Long? = null
)