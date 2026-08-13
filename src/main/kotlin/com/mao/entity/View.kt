package com.mao.entity

import java.time.LocalDate
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

data class UserProfileVo(
    val id: Int? = null,
    val userId: Int? = null,
    val userCode: String? = null,
    val realName: String? = null,
    val sexId: Int? = null,
    val entryDate: LocalDate? = null,
    val idCardNum: String? = null,
    val bloodTypeId: Int? = null,
    val high: Double? = null,
    val weight: Double? = null,
    val provinceId: Int? = null,
    val cityId: Int? = null,
    val districtId: Int? = null,
    val address: String? = null,
    val birthday: LocalDate? = null,
    val nationId: Int? = null,
    val countryId: Int? = null,
    val maritalId: Int? = null,
    val politicalId: Int? = null,
    val educationId: Int? = null,
    val major: String? = null,
    val originProvinceId: Int? = null,
    val originCityId: Int? = null,
    val originDistrictId: Int? = null,
    val originAddress: String? = null,
    val familyPhone: String? = null,
    val hobby: String? = null,
    val remark: String? = null
) : BaseDo()

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

/**
 * 系统信息
 */
data class ServerInfo(
    val serverName: String,         // 应用名称
    val version: String,            // 版本
    val startTime: LocalDateTime,   // 程序启动时间
    val liveTime: String,           // 程序活跃时间
    val totalRequests: Long,        // 总请求数
    val successResponse: Long,      // 成功请求书
    val errorResponse: Long,        // 失败请求数
    val avgResponse: Long,          // 平均响应时间
    val onlineUsers: Int,           // 在线用户数
    val loginUsers: Int,            // 登录用户数
)