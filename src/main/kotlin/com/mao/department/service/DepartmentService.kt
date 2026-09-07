package com.mao.department.service

import com.mao.department.entity.DepartmentCode
import com.mao.department.repository.DepartmentRepository
import org.springframework.stereotype.Service

@Service
class DepartmentService (
    private val departmentRepository: DepartmentRepository
) {

    fun getDepartmentThreeLevelCode(departmentId: Int): DepartmentCode {
        TODO()
    }

}