package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeMaterialAddQo
import com.mao.employee.entity.EmployeeMaterialDo
import tech.mappie.api.ObjectMappie

object EmployeeMaterialCreateMapper : ObjectMappie<EmployeeMaterialAddQo, EmployeeMaterialDo>() {

    override fun map(from: EmployeeMaterialAddQo): EmployeeMaterialDo = mapping {}

}