package com.mao.department.entity

import com.mao.common.entity.BaseVo
import com.mao.common.entity.Tree

data class DepartmentVo(
    override val id: Int? = null,
    val parentId: Int? = null,
    val departmentCode: String? = null,
    val departmentName: String? = null,
    val description: String? = null,
    val departmentType: Int? = null,
    val memberAssignable: Boolean? = null,
    val sortOrder: Int? = null,
    val status: Int? = null,
    override val children: List<DepartmentVo> = emptyList()
) : BaseVo(), Tree<DepartmentVo> {

    override val pid: Int?
        get() = parentId

    override fun withChildren(children: List<DepartmentVo>): DepartmentVo = copy(children = children)
}
