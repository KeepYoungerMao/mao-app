package com.mao.employee.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_employee_code_sequence")
data class EmployeeCodeSequenceDo(
    @Id
    var id: Int? = null,
    var sequenceYear: Int? = null,
    var departmentCode: String? = null,
    var currentValue: Int? = null,
    var createTime: LocalDateTime? = null,
    var updateTime: LocalDateTime? = null
)
