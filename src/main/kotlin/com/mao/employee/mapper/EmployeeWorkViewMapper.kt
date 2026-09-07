package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeWorkDo
import com.mao.employee.entity.EmployeeWorkVo
import tech.mappie.api.ObjectMappie

object EmployeeWorkViewMapper : ObjectMappie<EmployeeWorkDo, EmployeeWorkVo>() {

    override fun map(from: EmployeeWorkDo): EmployeeWorkVo = mapping {}

}