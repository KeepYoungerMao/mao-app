package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeRelationshipAddQo
import com.mao.employee.entity.EmployeeRelationshipDo
import tech.mappie.api.ObjectMappie

object EmployeeRelationshipCreateMapper : ObjectMappie<EmployeeRelationshipAddQo, EmployeeRelationshipDo>() {

    override fun map(from: EmployeeRelationshipAddQo): EmployeeRelationshipDo = mapping {}

}