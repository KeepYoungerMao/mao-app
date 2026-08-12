package com.mao.service

import com.mao.extension.RolePermissionData
import com.mao.repository.RolePermissionRefRepository
import com.mao.repository.UserRoleRefRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val userRoleRefRepository: UserRoleRefRepository,
    private val rolePermissionRefRepository: RolePermissionRefRepository
) {

    suspend fun getUserRolePermission(username: String): RolePermissionData? {
        // 查询用户拥有的角色
        val userRoles = userRoleRefRepository.getRoleByUsername(username)
            .filterNotNull()
            .filter { it.id != null && it.name != null }.toList()
        if (userRoles.isEmpty()) return null
        val roles = userRoles.mapNotNull { it.name }
        val roleIds = userRoles.mapNotNull { it.id }
        // 查询角色对应的权限
        val permissions = rolePermissionRefRepository.getPermissionByRoleIdIn(roleIds)
            .filterNotNull().filter { it.isNotBlank() }.toList()
        // 构建缓存
        return RolePermissionData(
            username = username,
            roles = roles,
            permissions = permissions
        )
    }

}