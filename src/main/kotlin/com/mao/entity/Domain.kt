package com.mao.entity

import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
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
    var passwordStatus: Int? = null,
) : BaseDo()

@Table("sys_user_role_ref")
data class UserRoleRefDo(
    @Id
    val id: Int?,
    val userId: Int,
    val roleId: Int
)

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

@Table("sys_role_permission_ref")
data class RolePermissionRefDo(
    @Id
    val id: Int,
    val roleId: Int,
    val permissionId: Int
)

@Table("sys_department")
data class DepartmentDo(
    @Id
    var id: Int? = null,
    var pid: Int? = null,
    var departmentName: String? = null,
    var description: String? = null,
) : BaseDo()

@Table("sys_user_department_ref")
data class UserDepartmentRefDo(
    @Id
    val id: Int?,
    val userId: Int,
    val departmentId: Int,
)

@Table("sys_user_profile")
data class UserProfileDo(
    @Id
    var id: Int? = null,
    var userId: Int? = null,
    var userCode: String? = null,
    var realName: String? = null,
    var sexId: Int? = null,
    var entryDate: LocalDate? = null,
    var idCardNum: String? = null,
    var bloodTypeId: Int? = null,
    var high: Double? = null,
    var weight: Double? = null,
    var provinceId: Int? = null,
    var cityId: Int? = null,
    var districtId: Int? = null,
    var address: String? = null,
    var birthday: LocalDate? = null,
    var nationId: Int? = null,
    var countryId: Int? = null,
    var maritalId: Int? = null,
    var politicalId: Int? = null,
    var educationId: Int? = null,
    var major: String? = null,
    var originProvinceId: Int? = null,
    var originCityId: Int? = null,
    var originDistrictId: Int? = null,
    var originAddress: String? = null,
    var familyPhone: String? = null,
    var hobby: String? = null,
    var remark: String? = null
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

/**
 * 系统服务指标数据
 */
@Table("sys_server_metric")
data class ServerMetric(
    @Id
    val id: Long,
    val minuteStart: LocalDateTime,
    val totalRequests: Long,
    val successRequests: Long,
    val errorRequests: Long,
    val totalResponseTimeMillis: Long,
    val avgResponseTimeMillis: Long,
    val onlineUsers: Int,
    val loginUsers: Int,
    val createdTime: LocalDateTime,
    @Transient
    @get:JvmName("getIsNewRecord")
    private val isNewRecord: Boolean = false
) : Persistable<Long> {

    override fun getId(): Long = id

    override fun isNew(): Boolean = isNewRecord

}