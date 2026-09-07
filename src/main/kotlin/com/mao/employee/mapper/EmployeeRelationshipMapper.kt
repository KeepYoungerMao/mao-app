package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeRelationshipDo
import com.mao.employee.entity.EmployeeRelationshipUpdateQo

object EmployeeRelationshipMapper {

    fun copyToExistDo(
        request: EmployeeRelationshipUpdateQo,
        relationship: EmployeeRelationshipDo
    ): EmployeeRelationshipDo = relationship.apply {
        request.realName?.let { realName = it }
        request.relationshipId?.let { relationshipId = it }
        request.idCardNum?.let { idCardNum = it }
        request.phone?.let { phone = it }
        request.remark?.let { remark = it }
    }

}