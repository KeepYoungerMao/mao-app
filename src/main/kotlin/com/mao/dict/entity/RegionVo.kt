package com.mao.dict.entity

import com.mao.common.entity.Tree

data class RegionVo(
    override val id: Int?,
    override val pid: Int?,
    val code: String?,
    val name: String?,
    override val children: List<RegionVo> = emptyList()
) : Tree<RegionVo> {
    override fun withChildren(children: List<RegionVo>): RegionVo =
        copy(children = children)
}
