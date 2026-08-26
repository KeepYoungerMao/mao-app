package com.mao.entity

import com.mao.extension.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 用户实体类
 */
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

/**
 * 用户资料实体类
 */
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

/**
 * 用户教育经历实体类
 */
@Table("sys_user_profile_education")
data class UserProfileEducationDo(
    @Id var id: Int? = null,
    var userId: Int? = null,
    var institutionName: String? = null,
    var degree: String? = null,
    var major: String? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var additionalInfo: String? = null
) : BaseDo()

/**
 * 用户工作经历实体类
 */
@Table("sys_user_profile_work")
data class UserProfileWorkDo(
    @Id var id: Int? = null,
    var userId: Int? = null,
    var companyName: String? = null,
    var jobTitle: String? = null,
    var industry: String? = null,
    var industryId: Int? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var responsibilities: String? = null,
    var currentEmployment: Boolean? = null
) : BaseDo()

/**
 * 用户人员关系实体类
 */
@Table("sys_user_profile_relationship")
data class UserProfileRelationshipDo(
    @Id var id: Int? = null,
    var userId: Int? = null,
    var realName: String? = null,
    var relationshipId: Int? = null,
    var idCardNum: String? = null,
    var phone: String? = null,
    var remark: String? = null
) : BaseDo()

/**
 * 用户上传材料实体类
 */
@Table("sys_user_profile_material")
data class UserProfileMaterialDo(
    @Id var id: Int? = null,
    var userId: Int? = null,
    var materialName: String? = null,
    var filePath: String? = null,
    var uploadTime: LocalDateTime? = null,
    var description: String? = null
) : BaseDo()

/**
 * 用户角色关联实体类
 */
@Table("sys_user_role_ref")
data class UserRoleRefDo(
    @Id 
    val id: Int?, 
    val userId: Int, 
    val roleId: Int
)

/**
 * 用户部门关联实体类
 */
@Table("sys_user_department_ref")
data class UserDepartmentRefDo(
    @Id 
    val id: Int?, 
    val userId: Int, 
    val departmentId: Int
)

/**
 * 密码状态枚举类
 * 0：正常，1：首次需要更改密码，2：密码已更改，3：密码已重置
 */
enum class PasswordStatus(val code: Int) {
    OK(0), 
    PASSWORD_UNCHANGE(1), 
    PASSWORD_EDIT(2), 
    PASSWORD_RESET(3)
}

/**
 * 用户查询结果包装类
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
 * 用户资料查询结果包装类
 */
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
) : BaseVo()

/**
 * 用户教育经历查询结果包装类
 */
data class UserProfileEducationVo(
    val id: Int? = null,
    val userId: Int? = null,
    val institutionName: String? = null,
    val degree: String? = null,
    val major: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val additionalInfo: String? = null
) : BaseVo()

/**
 * 用户工作经历查询结果包装类
 */
data class UserProfileWorkVo(
    val id: Int? = null,
    val userId: Int? = null,
    val companyName: String? = null,
    val jobTitle: String? = null,
    val industry: String? = null,
    val industryId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val responsibilities: String? = null,
    val currentEmployment: Boolean? = null
) : BaseVo()

/**
 * 用户人员关系查询结果包装类
 */
data class UserProfileRelationshipVo(
    val id: Int? = null,
    val userId: Int? = null,
    val realName: String? = null,
    val relationshipId: Int? = null,
    val idCardNum: String? = null,
    val phone: String? = null,
    val remark: String? = null
) : BaseVo()

/**
 * 用户上传材料查询结果包装类
 */
data class UserProfileMaterialVo(
    val id: Int? = null,
    val userId: Int? = null,
    val materialName: String? = null,
    val filePath: String? = null,
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
) : BaseVo()

/**
 * 用户详情查询结果包装类
 */
data class UserDetailVo(
    val user: UserVo,
    val profile: UserProfileVo,
    val roles: List<RoleVo>,
    val departments: List<DepartmentVo>
)

/**
 * 用户查询参数类
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
    val enabled: Boolean? = null, 
    val roleId: Int? = null, 
    val departmentId: Int? = null
) : PageQo()

/**
 * 新增用户请求参数类
 */
data class UserAddQo(
    @field:NotBlank 
    @field:Username 
    val username: String? = null,
    @field:NotBlank 
    @field:Length(max = 300) 
    val avatar: String? = null,
    @field:NotBlank 
    @field:Phone 
    val phone: String? = null,
    @field:NotBlank 
    @field:Length(max = 32) 
    @field:Email 
    val email: String? = null,
    @field:NotNull 
    val expireTime: LocalDateTime? = null,
    @field:NotBlank 
    @field:Length(min = 2, max = 20) 
    val realName: String? = null,
    @field:NotNull 
    val entryDate: LocalDate? = null,
    @field:NotBlank 
    @field:IdCard 
    val idCardNum: String? = null,
    @field:NotNull 
    val birthday: LocalDate? = null
)

/**
 * 更新用户请求参数类
 */
data class UserUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    @field:Username 
    val username: String? = null,
    @field:Length(max = 300) 
    val avatar: String? = null, 
    @field:Phone 
    val phone: String? = null,
    @field:Email 
    val email: String? = null
)

/**
 * 更新用户密码请求参数类
 */
data class UserPasswordUpdateQo(
    @field:NotBlank 
    val username: String? = null, 
    @field:NotBlank 
    val oldPassword: String? = null, 
    @field:NotBlank 
    val newPassword: String? = null, 
    val timestamp: Long? = null
)

/**
 * 重置用户密码请求参数类
 */
data class UserPasswordResetQo(
    @field:NotBlank 
    val username: String? = null
)

/**
 * 用户续期请求参数类
 */
data class UserRenewalQo(
    @field:NotBlank 
    val username: String? = null, 
    @field:NotNull 
    val expireTime: LocalDateTime? = null
)

/**
 * 用户角色更新请求参数类
 */
data class UserRoleUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    @field:NotEmpty 
    val roleIds: List<Int>? = null
)

/**
 * 用户资料更新请求参数类
 */
data class UserProfileUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    @field:Dict(DictType.SEX)
    val sexId: Int? = null, 
    val bloodTypeId: Int? = null, 
    val high: Double? = null, 
    val weight: Double? = null, 
    @field:ProvinceCityDistrict
    val provinceId: Int? = null, 
    @field:ProvinceCityDistrict
    val cityId: Int? = null, 
    @field:ProvinceCityDistrict
    val districtId: Int? = null, 
    val address: String? = null, 
    val birthday: LocalDate? = null, 
    @field:Dict(DictType.NATION)
    val nationId: Int? = null, 
    @field:Dict(DictType.COUNTRY)
    val countryId: Int? = null, 
    @field:Dict(DictType.MARITAL)
    val maritalId: Int? = null, 
    @field:Dict(DictType.POLITICAL)
    val politicalId: Int? = null, 
    @field:Dict(DictType.EDUCATION)
    val educationId: Int? = null, 
    val major: String? = null, 
    @field:ProvinceCityDistrict
    val originProvinceId: Int? = null, 
    @field:ProvinceCityDistrict
    val originCityId: Int? = null, 
    @field:ProvinceCityDistrict
    val originDistrictId: Int? = null, 
    val originAddress: String? = null, 
    val familyPhone: String? = null, 
    val hobby: String? = null, 
    val remark: String? = null
)

/**
 * 新增用户教育经历请求参数类
 */
data class UserProfileEducationAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val institutionName: String? = null,
    @field:Length(max = 30)
    val degree: String? = null,
    @field:Length(max = 50)
    val major: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val additionalInfo: String? = null
)

/**
 * 更新用户教育经历请求参数类
 */
data class UserProfileEducationUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 50)
    val institutionName: String? = null,
    @field:Length(max = 30)
    val degree: String? = null,
    @field:Length(max = 50)
    val major: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val additionalInfo: String? = null
)

/**
 * 新增用户工作经历请求参数类
 */
data class UserProfileWorkAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val companyName: String? = null,
    @field:Length(max = 30)
    val jobTitle: String? = null,
    @field:Length(max = 50)
    val industry: String? = null,
    val industryId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val responsibilities: String? = null,
    val currentEmployment: Boolean? = null
)

/**
 * 更新用户工作经历请求参数类
 */
data class UserProfileWorkUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 50)
    val companyName: String? = null,
    @field:Length(max = 30)
    val jobTitle: String? = null,
    @field:Length(max = 50)
    val industry: String? = null,
    val industryId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val responsibilities: String? = null,
    val currentEmployment: Boolean? = null
)

/**
 * 新增用户人员关系请求参数类
 */
data class UserProfileRelationshipAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 20)
    val realName: String? = null,
    @field:NotNull
    @field:Dict(DictType.RELATIONSHIP)
    val relationshipId: Int? = null,
    @field:IdCard
    val idCardNum: String? = null,
    @field:Phone
    val phone: String? = null,
    @field:Length(max = 300)
    val remark: String? = null
)

/**
 * 更新用户人员关系请求参数类
 */
data class UserProfileRelationshipUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 20)
    val realName: String? = null,
    @field:Dict(DictType.RELATIONSHIP)
    val relationshipId: Int? = null,
    @field:IdCard
    val idCardNum: String? = null,
    @field:Phone
    val phone: String? = null,
    @field:Length(max = 300)
    val remark: String? = null
)

/**
 * 新增用户上传材料请求参数类
 */
data class UserProfileMaterialAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val materialName: String? = null,
    @field:NotBlank
    @field:Length(max = 1000)
    val filePath: String? = null,
    @field:NotNull
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
)

/**
 * 更新用户上传材料请求参数类
 */
data class UserProfileMaterialUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 50)
    val materialName: String? = null,
    @field:Length(max = 1000)
    val filePath: String? = null,
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
)
