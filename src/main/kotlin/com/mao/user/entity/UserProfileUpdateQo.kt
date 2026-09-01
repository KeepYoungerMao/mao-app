package com.mao.user.entity

import com.mao.common.validate.DictField
import com.mao.common.validate.ProvinceCityDistrict
import com.mao.dict.entity.RegionType
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * 用户资料更新请求参数类
 */
data class UserProfileUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:DictField("SEX")
    val sexId: Int? = null,
    val bloodTypeId: Int? = null,
    val high: Double? = null,
    val weight: Double? = null,
    @field:ProvinceCityDistrict(type = RegionType.PROVINCE)
    val provinceId: Int? = null,
    @field:ProvinceCityDistrict(type = RegionType.CITY)
    val cityId: Int? = null,
    @field:ProvinceCityDistrict(type = RegionType.DISTRICT)
    val districtId: Int? = null,
    val address: String? = null,
    val birthday: LocalDate? = null,
    @field:DictField("NATION")
    val nationId: Int? = null,
    @field:DictField("COUNTRY")
    val countryId: Int? = null,
    @field:DictField("MARITAL")
    val maritalId: Int? = null,
    @field:DictField("POLITICAL")
    val politicalId: Int? = null,
    @field:DictField("EDUCATION")
    val educationId: Int? = null,
    val major: String? = null,
    @field:ProvinceCityDistrict(type = RegionType.PROVINCE)
    val originProvinceId: Int? = null,
    @field:ProvinceCityDistrict(type = RegionType.CITY)
    val originCityId: Int? = null,
    @field:ProvinceCityDistrict(type = RegionType.DISTRICT)
    val originDistrictId: Int? = null,
    val originAddress: String? = null,
    val familyPhone: String? = null,
    val hobby: String? = null,
    val remark: String? = null
)
