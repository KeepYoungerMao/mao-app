package com.mao.user.entity

import com.mao.department.entity.DepartmentVo
import com.mao.role.entity.RoleVo

/**
 * 用户详情查询结果包装类
 */
data class UserDetailVo(
    val user: UserVo,
    val profile: UserProfileVo,
    val roles: List<RoleVo>,
    val departments: List<DepartmentVo>
)
