package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeMaterialDo
import com.mao.employee.entity.EmployeeMaterialVo
import tech.mappie.api.ObjectMappie

object EmployeeMaterialViewMapper : ObjectMappie<EmployeeMaterialDo, EmployeeMaterialVo>() {

    override fun map(from: EmployeeMaterialDo): EmployeeMaterialVo = mapping {}

}