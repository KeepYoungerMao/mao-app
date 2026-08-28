package com.mao.user.entity

import com.mao.common.entity.PageQo
import com.mao.common.repository.QueryField

/**
 * 用户查询参数类
 */
data class UserQo(
    @QueryField(type = QueryField.Type.LIKE)
    val username: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val phone: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val email: String? = null,
    @QueryField
    val expired: Boolean? = null,
    @QueryField
    val locked: Boolean? = null,
    @QueryField
    val enabled: Boolean? = null,
    val roleId: Int? = null,
    val departmentId: Int? = null
) : PageQo()
