package com.mao.employee.entity

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * 新增用户部门关联请求参数类
 */
data class EmployeeDepartmentAddQo(
    @field:NotNull
    val employeeId: Int? = null,
    @field:NotNull
    val departmentId: Int? = null,
    @field:NotNull
    val positionId: Int? = null,
    val primaryAssignment: Boolean? = false,
    @field:NotNull
    val startDate: LocalDate? = null,
    @field:NotNull
    val endDate: LocalDate? = null
)
