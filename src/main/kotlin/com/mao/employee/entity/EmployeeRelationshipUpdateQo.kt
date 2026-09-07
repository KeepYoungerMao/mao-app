package com.mao.employee.entity

import com.mao.common.validate.DictField
import com.mao.common.validate.IdCard
import com.mao.common.validate.Phone
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * 更新用户人员关系请求参数类
 */
data class EmployeeRelationshipUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 20)
    val realName: String? = null,
    @field:DictField("RELATIONSHIP")
    val relationshipId: Int? = null,
    @field:IdCard
    val idCardNum: String? = null,
    @field:Phone
    val phone: String? = null,
    @field:Length(max = 300)
    val remark: String? = null
)
