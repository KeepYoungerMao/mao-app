package com.mao.employee.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDate

/**
 * 用户工作经历查询结果包装类
 */
data class EmployeeWorkVo(
    val id: Int? = null,
    val employeeId: Int? = null,
    val companyName: String? = null,
    val jobTitle: String? = null,
    val industry: String? = null,
    val industryId: Int? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val responsibilities: String? = null,
    val currentEmployment: Boolean? = null
) : BaseVo()
