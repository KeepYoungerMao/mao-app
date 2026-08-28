package com.mao.role.controller

import com.mao.common.handler.OperationLog
import com.mao.role.service.RoleService
import com.mao.log.entity.OperationModule
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/role")
@OperationLog(module = OperationModule.ROLE)
class RoleController (
    private val roleService: RoleService,
) {
}