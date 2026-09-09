package com.mao.department.entity

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class DepartmentStatusUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotNull
    @field:Min(0)
    @field:Max(2)
    val status: Int? = null
)
