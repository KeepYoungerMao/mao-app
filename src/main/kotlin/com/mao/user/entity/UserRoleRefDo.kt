package com.mao.user.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * 用户角色关联实体类
 */
@Table("sys_user_role_ref")
data class UserRoleRefDo(
    @Id
    val id: Int?,
    val userId: Int,
    val roleId: Int
)
