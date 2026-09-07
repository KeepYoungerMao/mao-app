package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeDepartmentAddQo
import com.mao.employee.entity.EmployeeDepartmentRefDo
import tech.mappie.api.ObjectMappie

object EmployeeDepartmentCreateMapper : ObjectMappie<EmployeeDepartmentAddQo, EmployeeDepartmentRefDo>() {

    override fun map(from: EmployeeDepartmentAddQo): EmployeeDepartmentRefDo = mapping {}

}