package com.mao.user.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDate

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
