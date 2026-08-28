package com.mao.user.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

/**
 * 用户角色更新请求参数类
 */
data class UserRoleUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:NotEmpty
    val roleIds: List<Int>? = null
)
