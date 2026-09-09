package com.mao.department.mapper

import com.mao.department.entity.DepartmentDo
import com.mao.department.entity.DepartmentUpdateQo

object DepartmentMapper {

    fun copyToExistDo(request: DepartmentUpdateQo, target: DepartmentDo): DepartmentDo = target.apply {
        request.departmentName?.let { departmentName = it }
        request.description?.let { description = it }
        request.memberAssignable?.let { memberAssignable = it }
    }

}
