package com.mao.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

enum class DictType(val id: Int) {
    SEX(1), NATION(2), MARITAL(3), COUNTRY(4), POLITICAL(5), EDUCATION(6),
    RELATIONSHIP(7), COMPANY_TYPE(8), BUSINESS_CONDITION(9), ANCIENT_BOOK_TYPE(10),
    DYNASTY(11), BOOK_STATUS(12), LIVE_TYPE(13)
}

@Table("sys_dict")
data class DictTypeDo(
    @Id 
    var id: Int? = null, 
    var name: String? = null, 
    var description: String? = null
)

@Table("sys_dict_item")
data class DictItemDo(
    @Id 
    var id: Int? = null, 
    var pid: Int? = null, 
    var name: String? = null,
    var status: Int? = null
) : BaseDo()

@Table("sys_province_city_district")
data class ProvinceCityDistrictDo(
    @Id 
    var id: Int? = null, 
    var pid: Int? = null, 
    var code: String? = null, 
    var name: String? = null
)

@Table("sys_industry_2017")
data class IndustryDo(
    @Id 
    var id: Int? = null, 
    var pid: Int? = null, 
    var code: String? = null, 
    var name: String? = null, 
    var description: String? = null
)

data class DictItemAddQo(
    @field:NotNull 
    val pid: Int? = null, 
    @field:NotBlank
    @field:Length(max = 100) 
    val name: String? = null
)

data class DictItemUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    @field:NotBlank 
    @field:Length(max = 100) 
    val name: String? = null
)

data class DictTypeVo(
    val id: Int?, 
    val name: String?, 
    val description: String?
)

data class DictItemVo(
    val id: Int?, 
    val pid: Int?, 
    val name: String?,
    val status: Int? = null
)

data class DictGroupVo(
    val type: DictTypeVo, 
    val items: List<DictItemVo>
)

data class ProvinceCityDistrictVo(
    override val id: Int?,
    override val pid: Int?,
    val code: String?,
    val name: String?,
    override val children: List<ProvinceCityDistrictVo> = emptyList()
) : Tree<ProvinceCityDistrictVo> {
    override fun withChildren(children: List<ProvinceCityDistrictVo>): ProvinceCityDistrictVo =
        copy(children = children)
}

data class IndustryVo(
    override val id: Int?,
    override val pid: Int?,
    val code: String?,
    val name: String?,
    val description: String?,
    override val children: List<IndustryVo> = emptyList()
) : Tree<IndustryVo> {
    override fun withChildren(children: List<IndustryVo>): IndustryVo = copy(children = children)
}
