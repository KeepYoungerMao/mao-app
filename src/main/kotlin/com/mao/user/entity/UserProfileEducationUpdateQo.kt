package com.mao.user.entity

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDate

/**
 * 更新用户教育经历请求参数类
 */
data class UserProfileEducationUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 50)
    val institutionName: String? = null,
    @field:Length(max = 30)
    val degree: String? = null,
    @field:Length(max = 50)
    val major: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val additionalInfo: String? = null
)
