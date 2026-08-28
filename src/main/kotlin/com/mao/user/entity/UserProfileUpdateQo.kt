package com.mao.user.entity

import com.mao.common.validate.Dict
import com.mao.common.validate.ProvinceCityDistrict
import com.mao.dict.entity.DictType
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

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
