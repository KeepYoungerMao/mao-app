package com.mao.user.entity

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * 更新用户部门关联请求参数类。未传入的基础字段保持原值。
 */
data class UserDepartmentUpdateQo(
    @field:NotNull
    val id: Int? = null,
    val positionId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val primaryAssignment: Boolean? = null,
    val enabled: Boolean? = null
)
