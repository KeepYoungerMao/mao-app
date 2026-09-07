package com.mao.employee.entity

import com.mao.common.entity.PageQo
import com.mao.common.repository.QueryField
import java.time.LocalDate

data class EmployeeQo(
    @QueryField
    val userId: Int? = null,
    @QueryField
    val employeeCode: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val realName: String? = null,
    @QueryField
    val sexId: Int? = null,
    @QueryField(name = "entryDate", type = QueryField.Type.GREATER_THAN)
    val entryDateStart: LocalDate? = null,
    @QueryField(name = "entryDate", type = QueryField.Type.LESS_THAN)
    val entryDateEnd: LocalDate? = null,
    @QueryField
    val idCardNum: String? = null,
    @QueryField
    val bloodTypeId: Int? = null,
    @QueryField
    val provinceId: Int? = null,
    @QueryField
    val cityId: Int? = null,
    @QueryField
    val districtId: Int? = null,
    @QueryField
    val nationId: Int? = null,
    @QueryField
    val countryId: Int? = null,
    @QueryField
    val maritalId: Int? = null,
    @QueryField
    val politicalId: Int? = null,
    @QueryField
    val educationId: Int? = null,
    @QueryField
    val originProvinceId: Int? = null,
    @QueryField
    val originCityId: Int? = null,
    @QueryField
    val originDistrictId: Int? = null,
) : PageQo()
