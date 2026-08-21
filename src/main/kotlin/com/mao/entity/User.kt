package com.mao.entity

import com.mao.extension.IdCard
import com.mao.extension.Phone
import com.mao.extension.QueryField
import com.mao.extension.Username
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("sys_user")
data class UserDo(
    @Id var id: Int? = null,
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

@Table("sys_user_profile")
data class UserProfileDo(
    @Id var id: Int? = null,
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

@Table("sys_user_role_ref")
data class UserRoleRefDo(@Id val id: Int?, val userId: Int, val roleId: Int)

@Table("sys_user_department_ref")
data class UserDepartmentRefDo(@Id val id: Int?, val userId: Int, val departmentId: Int)

enum class PasswordStatus(val code: Int) {
    OK(0), PASSWORD_UNCHANGE(1), PASSWORD_EDIT(2), PASSWORD_RESET(3)
}

data class UserVo(
    val id: Int? = null, val username: String? = null, val avatar: String? = null,
    val phone: String? = null, val email: String? = null, val expired: Boolean? = null,
    val locked: Boolean? = null, val enabled: Boolean? = null,
    val expireTime: LocalDateTime? = null, val lastLoginTime: LocalDateTime? = null
) : BaseVo()

data class UserProfileVo(
    val id: Int? = null, val userId: Int? = null, val userCode: String? = null,
    val realName: String? = null, val sexId: Int? = null, val entryDate: LocalDate? = null,
    val idCardNum: String? = null, val bloodTypeId: Int? = null, val high: Double? = null,
    val weight: Double? = null, val provinceId: Int? = null, val cityId: Int? = null,
    val districtId: Int? = null, val address: String? = null, val birthday: LocalDate? = null,
    val nationId: Int? = null, val countryId: Int? = null, val maritalId: Int? = null,
    val politicalId: Int? = null, val educationId: Int? = null, val major: String? = null,
    val originProvinceId: Int? = null, val originCityId: Int? = null, val originDistrictId: Int? = null,
    val originAddress: String? = null, val familyPhone: String? = null, val hobby: String? = null,
    val remark: String? = null
) : BaseVo()

data class UserDetailVo(val user: UserVo, val profile: UserProfileVo, val roles: List<RoleVo>, val departments: List<DepartmentVo>)

data class UserQo(
    @QueryField(type = QueryField.Type.LIKE) val username: String? = null,
    @QueryField(type = QueryField.Type.LIKE) val phone: String? = null,
    @QueryField(type = QueryField.Type.LIKE) val email: String? = null,
    @QueryField val expired: Boolean? = null, @QueryField val locked: Boolean? = null,
    @QueryField val enabled: Boolean? = null, val roleId: Int? = null, val departmentId: Int? = null
) : PageQo()

data class UserAddQo(
    @field:NotBlank @field:Username val username: String? = null,
    @field:NotBlank @field:Length(max = 300) val avatar: String? = null,
    @field:NotBlank @field:Phone val phone: String? = null,
    @field:NotBlank @field:Length(max = 32) @field:Email val email: String? = null,
    @field:NotNull val expireTime: LocalDateTime? = null,
    @field:NotBlank @field:Length(min = 2, max = 20) val realName: String? = null,
    @field:NotNull val entryDate: LocalDate? = null,
    @field:NotBlank @field:IdCard val idCardNum: String? = null,
    @field:NotNull val birthday: LocalDate? = null
)

data class UserUpdateQo(
    @field:NotNull val id: Int? = null, @field:Username val username: String? = null,
    @field:Length(max = 300) val avatar: String? = null, @field:Phone val phone: String? = null,
    @field:Email val email: String? = null
)

data class UserPasswordUpdateQo(@field:NotBlank val username: String? = null, @field:NotBlank val oldPassword: String? = null, @field:NotBlank val newPassword: String? = null, val timestamp: Long? = null)
data class UserPasswordResetQo(@field:NotBlank val username: String? = null)
data class UserRenewalQo(@field:NotBlank val username: String? = null, @field:NotNull val expireTime: LocalDateTime? = null)
data class UserRoleUpdateQo(@field:NotNull val id: Int? = null, @field:NotEmpty val roleIds: List<Int>? = null)
data class UserProfileUpdateQo(@field:NotNull val id: Int? = null, val sexId: Int? = null, val bloodTypeId: Int? = null, val high: Double? = null, val weight: Double? = null, val provinceId: Int? = null, val cityId: Int? = null, val districtId: Int? = null, val address: String? = null, val birthday: LocalDate? = null, val nationId: Int? = null, val countryId: Int? = null, val maritalId: Int? = null, val politicalId: Int? = null, val educationId: Int? = null, val major: String? = null, val originProvinceId: Int? = null, val originCityId: Int? = null, val originDistrictId: Int? = null, val originAddress: String? = null, val familyPhone: String? = null, val hobby: String? = null, val remark: String? = null)
