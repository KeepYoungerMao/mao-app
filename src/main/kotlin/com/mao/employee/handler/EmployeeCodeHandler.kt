package com.mao.employee.handler

import com.mao.department.service.DepartmentService
import org.springframework.stereotype.Service

@Service
class EmployeeCodeHandler(
    private val departmentService: DepartmentService,
) {

    fun generateDeployeeCode() : String {
        TODO()
    }

}