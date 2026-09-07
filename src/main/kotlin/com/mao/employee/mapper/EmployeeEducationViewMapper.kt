package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeEducationDo
import com.mao.employee.entity.EmployeeEducationVo
import tech.mappie.api.ObjectMappie

object EmployeeEducationViewMapper : ObjectMappie<EmployeeEducationDo, EmployeeEducationVo>() {

    override fun map(from: EmployeeEducationDo): EmployeeEducationVo = mapping {}

}