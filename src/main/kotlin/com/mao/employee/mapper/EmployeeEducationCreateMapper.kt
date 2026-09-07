package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeEducationAddQo
import com.mao.employee.entity.EmployeeEducationDo
import tech.mappie.api.ObjectMappie

object EmployeeEducationCreateMapper : ObjectMappie<EmployeeEducationAddQo, EmployeeEducationDo>() {

    override fun map(from: EmployeeEducationAddQo): EmployeeEducationDo = mapping {}

}