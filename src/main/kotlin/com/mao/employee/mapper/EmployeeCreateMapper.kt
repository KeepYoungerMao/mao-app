package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeAddQo
import com.mao.employee.entity.EmployeeDo
import tech.mappie.api.ObjectMappie

object EmployeeCreateMapper : ObjectMappie<EmployeeAddQo, EmployeeDo>() {

    override fun map(from: EmployeeAddQo): EmployeeDo = mapping {}

}