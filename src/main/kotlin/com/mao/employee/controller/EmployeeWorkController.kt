package com.mao.employee.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.employee.entity.EmployeeWorkAddQo
import com.mao.employee.entity.EmployeeWorkUpdateQo
import com.mao.employee.entity.EmployeeWorkVo
import com.mao.employee.service.EmployeeWorkService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employee/work")
@OperationLog(module = OperationModule.EMPLOYEE_WORK)
class EmployeeWorkController(
    private val employeeWorkService: EmployeeWorkService
) {

    @PostMapping("all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchEmployeeWorks(@Valid @RequestBody request: IdQo<Int>): List<EmployeeWorkVo> {
        return employeeWorkService.searchEmployeeWorks(request.id)
    }

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createEmployeeWork(@Valid @RequestBody request: EmployeeWorkAddQo): EmployeeWorkVo {
        return employeeWorkService.createEmployeeWork(request)
    }

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateEmployeeWork(@Valid @RequestBody request: EmployeeWorkUpdateQo): EmployeeWorkVo {
        return employeeWorkService.updateEmployeeWork(request)
    }

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteEmployeeWork(@Valid @RequestBody request: IdQo<Int>): Tips {
        return employeeWorkService.deleteEmployeeWork(request.id)
    }

}