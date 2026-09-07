package com.mao.employee.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * 用户上传材料实体类
 */
@Table("sys_employee_material")
data class EmployeeMaterialDo(
    @Id
    var id: Int? = null,
    var employeeId: Int? = null,
    var materialName: String? = null,
    var filePath: String? = null,
    var uploadTime: LocalDateTime? = null,
    var description: String? = null
) : BaseDo()
