package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeEducationDo
import com.mao.employee.entity.EmployeeEducationUpdateQo

object EmployeeEducationMapper {

    fun copyToExistDo(request: EmployeeEducationUpdateQo, education: EmployeeEducationDo): EmployeeEducationDo = education.apply {
        request.institutionName?.let { institutionName = it }
        request.degree?.let { degree = it }
        request.major?.let { major = it }
        request.startDate?.let { startDate = it }
        request.endDate?.let { endDate = it }
        request.additionalInfo?.let { additionalInfo = it }
    }

}