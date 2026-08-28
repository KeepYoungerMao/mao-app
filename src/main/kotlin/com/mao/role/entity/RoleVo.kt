package com.mao.role.entity

import com.mao.common.entity.BaseVo

/**
 * 角色信息实体类
 */
data class RoleVo(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null
) : BaseVo()
