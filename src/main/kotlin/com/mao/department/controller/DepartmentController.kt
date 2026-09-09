package com.mao.department.controller

import com.mao.common.entity.IdQo
import com.mao.common.entity.Tips
import com.mao.common.handler.OperationLog
import com.mao.department.entity.*
import com.mao.department.service.DepartmentService
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/department")
@OperationLog(module = OperationModule.DEPARTMENT)
class DepartmentController (
    private val departmentService: DepartmentService
) {

    @PostMapping("tree")
    @OperationLog(operation = Operation.PAGE)
    suspend fun getDepartmentTree(): List<DepartmentVo> {
        return departmentService.getDepartmentTree()
    }

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun createDepartment(@Valid @RequestBody request: DepartmentAddQo): DepartmentVo {
        return departmentService.createDepartment(request)
    }

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateDepartment(@Valid @RequestBody request: DepartmentUpdateQo): DepartmentVo {
        return departmentService.updateDepartment(request)
    }

    @PostMapping("status/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateDepartmentStatus(@Valid @RequestBody request: DepartmentStatusUpdateQo): Tips {
        return departmentService.updateDepartmentStatus(request)
    }

    @PostMapping("sort/update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateDepartmentSort(@Valid @RequestBody request: List<@Valid DepartmentSortUpdateQo>): Tips {
        return departmentService.updateDepartmentSort(request)
    }

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteDepartment(@Valid @RequestBody request: IdQo<Int>): Tips {
        return departmentService.deleteDepartment(request.id)
    }

}
