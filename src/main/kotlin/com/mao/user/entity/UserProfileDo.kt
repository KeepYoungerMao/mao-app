package com.mao.user.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

/**
 * 用户资料实体类
 */
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
