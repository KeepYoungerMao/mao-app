package com.mao.employee.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.PageResponse
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.employee.entity.*
import com.mao.employee.service.EmployeeService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employee")
@OperationLog(module = OperationModule.EMPLOYEE)
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping("page")
    @OperationLog(operation = Operation.PAGE)
    suspend fun searchEmployees(@RequestBody request: EmployeeQo): PageResponse<EmployeeVo> {
        return employeeService.searchEmployees(request)
    }

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createEmployee(@Valid @RequestBody request: EmployeeAddQo): EmployeeVo {
        return employeeService.createEmployee(request)
    }

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateEmployee(@Valid @RequestBody request: EmployeeUpdateQo): EmployeeVo {
        return employeeService.updateEmployee(request)
    }

    @PostMapping("department/create")
    @OperationLog(module = OperationModule.EMPLOYEE_DEPARTMENT, operation = Operation.CREATE)
    suspend fun createEmployeeDepartment(@Valid @RequestBody request: EmployeeDepartmentAddQo): Tips {
        return employeeService.createEmployeeDepartment(request)
    }

    @PostMapping("department/update")
    @OperationLog(module = OperationModule.EMPLOYEE_DEPARTMENT, operation = Operation.UPDATE)
    suspend fun updateUserDepartment(@Valid @RequestBody request: EmployeeDepartmentUpdateQo): Tips {
        return employeeService.updateUserDepartment(request)
    }

    @PostMapping("user/unbind")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun unbindUser(@Valid @RequestBody request: IdQo<Int>) : Tips {
        return employeeService.unbindUser(request.id)
    }

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteEmployee(@Valid @RequestBody request: IdQo<Int>) : Tips {
        return employeeService.deleteEmployee(request.id)
    }

}