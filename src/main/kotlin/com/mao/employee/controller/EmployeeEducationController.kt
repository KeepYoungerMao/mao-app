package com.mao.employee.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.employee.entity.EmployeeEducationAddQo
import com.mao.employee.entity.EmployeeEducationUpdateQo
import com.mao.employee.entity.EmployeeEducationVo
import com.mao.employee.service.EmployeeEducationService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employee/education")
@OperationLog(module = OperationModule.EMPLOYEE_EDUCATION)
class EmployeeEducationController(
    private val employeeEducationService: EmployeeEducationService
) {

    @PostMapping("all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchEmployeeEducations(@Valid @RequestBody request: IdQo<Int>): List<EmployeeEducationVo> {
        return employeeEducationService.searchEmployeeEducations(request.id)
    }

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createEmployeeEducation(@Valid @RequestBody request: EmployeeEducationAddQo): EmployeeEducationVo {
        return employeeEducationService.createEmployeeEducation(request)
    }

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateEmployeeEducation(@Valid @RequestBody request: EmployeeEducationUpdateQo): EmployeeEducationVo {
        return employeeEducationService.updateEmployeeEducation(request)
    }

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteEmployeeEducation(@Valid @RequestBody request: IdQo<Int>): Tips {
        return employeeEducationService.deleteEmployeeEducation(request.id)
    }

}