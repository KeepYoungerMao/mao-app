package com.mao.department.entity

import com.mao.common.entity.BaseVo

data class DepartmentVo(
    val id: Int? = null,
    val parentId: Int? = null,
    val departmentCode: String? = null,
    val departmentName: String? = null,
    val description: String? = null,
    val departmentType: Int? = null,
    val memberAssignable: Boolean? = null,
    val sortOrder: Int? = null,
    val status: Int? = null
) : BaseVo()
