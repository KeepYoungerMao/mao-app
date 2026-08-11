package com.mao.entity

import com.mao.extension.QueryField
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 根据 id 查询数据
 */
data class IdQo<T>(val id: T?)

/**
 * 分页查询参数
 */
open class PageQo {

    // 页码
    var pageNum: Int = 1

    // 每页条数
    var pageSize: Int = 10

    // 是否需要重新查询数量
    var recount: Boolean = true

    // 上次查询的数量，查询条件不变时，可设recount=false，并传递该参数，以减少SQL执行
    var lastHitCount: Long? = null

    fun offset(): Int = (pageNum - 1) * pageSize
}

/**
 * 用户数据查询参数
 */
data class UserQo(
    @QueryField(type = QueryField.Type.LIKE)
    val username: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val phone: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val email: String? = null,
    @QueryField
    val expired: Boolean? = null,
    @QueryField
    val locked: Boolean? = null,
    @QueryField
    val enabled: Boolean? = null
) : PageQo()

/**
 * 新增用户数据参数
 * 包含账号数据和用户资料数据
 */
data class UserAddQo(
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expireTime: LocalDateTime? = null,
    val realName: String? = null,
    val entryDate: LocalDate? = null,
    val idCardNum: String? = null,
    val birthday: LocalDate? = null,
)

/**
 * 更新用户数据参数
 */
data class UserUpdateQo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
)

/**
 * 用户密码更新参数
 */
data class UserPasswordUpdateQo(
    val username: String? = null,
    val oldPassword: String? = null,
    val newPassword: String? = null,
    val timestamp: Long? = null
)

/**
 * 用户密码重置参数
 */
data class UserPasswordResetQo(val username: String? = null)

/**
 * 用户续期参数
 */
data class UserRenewalQo(val username: String? = null, val expireTime: LocalDateTime? = null)

/**
 * 用户更新角色信息参数
 */
data class UserRoleUpdateQo(val id: Int? = null, val roleIds: List<Int>? = null)

/**
 * 角色数据查询参数
 */
data class RoleQo(
    @QueryField(type = QueryField.Type.LIKE)
    val name: String? = null,
) : PageQo()

/**
 * 用户资料更新数据
 */
data class UserProfileUpdateQo(
    val id: Int? = null,
    val sexId: Int? = null,
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
)

/**
 * 系统操作日志查询参数
 */
data class OperationLogQo(
    @QueryField
    var username: String? = null,
    @QueryField
    var module: String? = null,
    @QueryField
    var operation: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    var ip: String? = null,
    @QueryField
    var success: Boolean? = null
) : PageQo()