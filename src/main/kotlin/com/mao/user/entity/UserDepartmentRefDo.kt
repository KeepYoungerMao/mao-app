package com.mao.user.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

/**
 * 用户部门关联实体类
 */
@Table("sys_user_department_ref")
data class UserDepartmentRefDo(
    @Id
    val id: Int?,
    val userId: Int,
    val departmentId: Int,
    var positionId: Int? = null,
    var primaryAssignment: Boolean? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var enabled: Boolean? = null
) : BaseDo()
