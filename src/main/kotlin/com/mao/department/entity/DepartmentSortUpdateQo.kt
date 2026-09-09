package com.mao.department.entity

import jakarta.validation.constraints.NotNull

/**
 * 部门排序更新请求。一次请求中的部门必须属于同一父级部门。
 */
data class DepartmentSortUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotNull
    val sortOrder: Int? = null
)
