package com.mao.department.controller

import com.mao.department.service.DepartmentService
import com.mao.common.handler.OperationLog
import com.mao.log.entity.OperationModule
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/department")
@OperationLog(module = OperationModule.DEPARTMENT)
class DepartmentController (
    private val departmentService: DepartmentService
) {
}