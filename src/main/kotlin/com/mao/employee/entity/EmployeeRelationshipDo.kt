package com.mao.employee.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * 用户人员关系实体类
 */
@Table("sys_employee_relationship")
data class EmployeeRelationshipDo(
    @Id
    var id: Int? = null,
    var employeeId: Int? = null,
    var realName: String? = null,
    var relationshipId: Int? = null,
    var idCardNum: String? = null,
    var phone: String? = null,
    var remark: String? = null
) : BaseDo()
