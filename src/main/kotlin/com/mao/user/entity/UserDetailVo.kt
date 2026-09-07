package com.mao.user.entity

import com.mao.role.entity.RoleVo

/**
 * 用户详情查询结果包装类
 */
data class UserDetailVo(
    val user: UserVo,
    val roles: List<RoleVo>
)
