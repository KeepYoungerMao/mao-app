package com.mao.employee.entity

import com.mao.common.entity.BaseVo

/**
 * 用户人员关系查询结果包装类
 */
data class EmployeeRelationshipVo(
    val id: Int? = null,
    val employeeId: Int? = null,
    val realName: String? = null,
    val relationshipId: Int? = null,
    val idCardNum: String? = null,
    val phone: String? = null,
    val remark: String? = null
) : BaseVo()
