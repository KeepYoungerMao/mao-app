package com.mao.employee.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

/**
 * 用户部门关联实体类
 */
@Table("sys_employee_department_ref")
data class EmployeeDepartmentRefDo(
    @Id
    var id: Int? = null,
    var employeeId: Int? = null,
    var departmentId: Int? = null,
    var positionId: Int? = null,
    var primaryAssignment: Boolean? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var enabled: Boolean? = null
) : BaseDo()
