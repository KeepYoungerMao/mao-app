package com.mao.department.entity

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class DepartmentUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 100)
    val departmentName: String? = null,
    @field:Length(max = 500)
    val description: String? = null,
    // Kept for backward compatibility; department type cannot be changed.
    val departmentType: Int? = null,
    val memberAssignable: Boolean? = null
)
