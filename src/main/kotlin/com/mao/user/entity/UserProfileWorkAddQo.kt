package com.mao.user.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

/**
 * 新增用户工作经历请求参数类
 */
data class UserProfileWorkAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val companyName: String? = null,
    @field:Length(max = 30)
    val jobTitle: String? = null,
    @field:Length(max = 50)
    val industry: String? = null,
    val industryId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val responsibilities: String? = null,
    val currentEmployment: Boolean? = null
)
