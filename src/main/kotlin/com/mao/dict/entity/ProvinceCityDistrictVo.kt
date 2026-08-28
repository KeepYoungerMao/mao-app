package com.mao.dict.entity

import com.mao.common.entity.Tree

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
