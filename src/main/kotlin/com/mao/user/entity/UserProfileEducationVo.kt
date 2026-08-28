package com.mao.user.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDate

/**
 * 用户教育经历查询结果包装类
 */
data class UserProfileEducationVo(
    val id: Int? = null,
    val userId: Int? = null,
    val institutionName: String? = null,
    val degree: String? = null,
    val major: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val additionalInfo: String? = null
) : BaseVo()
