package com.mao.employee.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.employee.entity.EmployeeRelationshipAddQo
import com.mao.employee.entity.EmployeeRelationshipUpdateQo
import com.mao.employee.entity.EmployeeRelationshipVo
import com.mao.employee.service.EmployeeRelationshipService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employee/relationship")
@OperationLog(module = OperationModule.EMPLOYEE_RELATIONSHIP)
class EmployeeRelationshipController(
    private val employeeRelationshipService: EmployeeRelationshipService
) {

    @PostMapping("all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchEmployeeRelationships(@Valid @RequestBody request: IdQo<Int>): List<EmployeeRelationshipVo> {
        return employeeRelationshipService.searchEmployeeRelationships(request.id)
    }

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createEmployeeRelationship(@Valid @RequestBody request: EmployeeRelationshipAddQo): EmployeeRelationshipVo {
        return employeeRelationshipService.createEmployeeRelationship(request)
    }

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateEmployeeRelationship(
        @Valid @RequestBody request: EmployeeRelationshipUpdateQo
    ): EmployeeRelationshipVo {
        return employeeRelationshipService.updateEmployeeRelationship(request)
    }

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteEmployeeRelationship(@Valid @RequestBody request: IdQo<Int>): Tips {
        return employeeRelationshipService.deleteEmployeeRelationship(request.id)
    }

}