package com.mao.employee.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

/**
 * 用户教育经历实体类
 */
@Table("sys_employee_education")
data class EmployeeEducationDo(
    @Id
    var id: Int? = null,
    var employeeId: Int? = null,
    var institutionName: String? = null,
    var degree: String? = null,
    var major: String? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var additionalInfo: String? = null
) : BaseDo()
