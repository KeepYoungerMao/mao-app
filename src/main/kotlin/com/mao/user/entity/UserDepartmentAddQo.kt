package com.mao.user.entity

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * 新增用户部门关联请求参数类
 */
data class UserDepartmentAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotNull
    val departmentId: Int? = null,
    val positionId: Int? = null,
    val primaryAssignment: Boolean? = false,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)
