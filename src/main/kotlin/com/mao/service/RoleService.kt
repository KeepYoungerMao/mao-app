package com.mao.service

import com.mao.repository.RolePermissionRefRepository
import com.mao.repository.UserRoleRefRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val userRoleRefRepository: UserRoleRefRepository,
    private val rolePermissionRefRepository: RolePermissionRefRepository
) {



}