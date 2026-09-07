package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeWorkAddQo
import com.mao.employee.entity.EmployeeWorkDo
import tech.mappie.api.ObjectMappie

object EmployeeWorkCreateMapper : ObjectMappie<EmployeeWorkAddQo, EmployeeWorkDo>() {

    override fun map(from: EmployeeWorkAddQo): EmployeeWorkDo = mapping {}

}