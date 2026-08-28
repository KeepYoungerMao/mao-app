package com.mao.role.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_role_permission_ref")
data class RolePermissionRefDo(
    @Id
    val id: Int,
    val roleId: Int,
    val permissionId: Int
)
