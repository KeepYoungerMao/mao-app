package com.mao.role.entity

import jakarta.validation.constraints.NotNull

data class RolePermissionUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    val permissionIds: List<Int>? = null
)
