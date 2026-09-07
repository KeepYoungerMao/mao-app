package com.mao.employee.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.employee.entity.EmployeeMaterialAddQo
import com.mao.employee.entity.EmployeeMaterialUpdateQo
import com.mao.employee.entity.EmployeeMaterialVo
import com.mao.employee.service.EmployeeMaterialService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/employee/work")
@OperationLog(module = OperationModule.EMPLOYEE_MATERIAL)
class EmployeeMaterialController(
    private val employeeMaterialService: EmployeeMaterialService
) {

    @PostMapping("profile/material/all")
    @OperationLog(operation = Operation.ALL)
    suspend fun searchEmployeeMaterials(@Valid @RequestBody request: IdQo<Int>): List<EmployeeMaterialVo> {
        return employeeMaterialService.searchEmployeeMaterials(request.id)
    }

    @PostMapping("profile/material/create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createEmployeeMaterial(@Valid @RequestBody request: EmployeeMaterialAddQo): EmployeeMaterialVo {
        return employeeMaterialService.createEmployeeMaterial(request)
    }

    @PostMapping("profile/material/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateEmployeeMaterial(@Valid @RequestBody request: EmployeeMaterialUpdateQo): EmployeeMaterialVo {
        return employeeMaterialService.updateEmployeeMaterial(request)
    }

    @PostMapping("profile/material/delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteEmployeeMaterial(@Valid @RequestBody request: IdQo<Int>): Tips {
        return employeeMaterialService.deleteEmployeeMaterial(request.id)
    }

}