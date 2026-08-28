package com.mao.dict.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_province_city_district")
data class ProvinceCityDistrictDo(
    @Id
    var id: Int? = null,
    var pid: Int? = null,
    var code: String? = null,
    var name: String? = null
)
