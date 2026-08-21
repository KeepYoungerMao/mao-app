package com.mao.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/** 系统预置字典类型。枚举值与数据库 sys_dict.id 一一对应。 */
enum class DictType(val id: Int) {
    SEX(1), NATION(2), MARITAL(3), COUNTRY(4), POLITICAL(5), EDUCATION(6),
    RELATIONSHIP(7), COMPANY_TYPE(8), BUSINESS_CONDITION(9), ANCIENT_BOOK_TYPE(10),
    DYNASTY(11), BOOK_STATUS(12), LIVE_TYPE(13),
}

@Table("sys_dict")
data class DictTypeDo(
    @Id var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
)

@Table("sys_dict_item")
data class DictItemDo(
    @Id var id: Int? = null,
    var pid: Int? = null,
    var name: String? = null,
) : BaseDo()

@Table("sys_province_city_district")
data class ProvinceCityDistrictDo(
    @Id var id: Int? = null,
    var pid: Int? = null,
    var code: String? = null,
    var name: String? = null,
)

@Table("sys_industry_2017")
data class IndustryDo(
    @Id var id: Int? = null,
    var pid: Int? = null,
    var code: String? = null,
    var name: String? = null,
    var description: String? = null,
)