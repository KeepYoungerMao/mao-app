package com.mao.employee.entity

import com.mao.common.validate.DictField
import com.mao.common.validate.IdCard
import com.mao.common.validate.RegionField
import com.mao.dict.entity.RegionType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class EmployeeAddQo(
    @field:NotNull
    val departmentId: Int? = null,
    @field:NotBlank
    val realName: String? = null,
    @field:DictField("SEX")
    val sexId: Int? = null,
    val entryDate: LocalDate? = null,
    @field:NotBlank
    @field:IdCard
    val idCardNum: String? = null,
    val bloodTypeId: Int? = null,
    val high: Double? = null,
    val weight: Double? = null,
    @field:RegionField(type = RegionType.PROVINCE)
    val provinceId: Int? = null,
    @field:RegionField(type = RegionType.CITY)
    val cityId: Int? = null,
    @field:RegionField(type = RegionType.DISTRICT)
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
    @field:RegionField(type = RegionType.PROVINCE)
    val originProvinceId: Int? = null,
    @field:RegionField(type = RegionType.CITY)
    val originCityId: Int? = null,
    @field:RegionField(type = RegionType.DISTRICT)
    val originDistrictId: Int? = null,
    val originAddress: String? = null,
    val familyPhone: String? = null,
    val hobby: String? = null,
    val remark: String? = null
)
