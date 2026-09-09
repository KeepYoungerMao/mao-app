package com.mao.department.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

data class DepartmentAddQo(
    val parentId: Int? = null,
    @field:NotBlank
    @field:Pattern(regexp = "\\d{2}", message = "部门编号必须是两位数字")
    val departmentCode: String? = null,
    @field:NotBlank
    @field:Length(max = 100)
    val departmentName: String? = null,
    @field:Length(max = 500)
    val description: String? = null,
    val memberAssignable: Boolean? = false,
    val sortOrder: Int? = null
)
