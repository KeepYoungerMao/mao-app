package com.mao.dict.entity

import com.mao.common.entity.Tree

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
