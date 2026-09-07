package com.mao.employee.mapper

import com.mao.common.util.DesensitizeUtils
import com.mao.employee.entity.EmployeeRelationshipDo
import com.mao.employee.entity.EmployeeRelationshipVo
import tech.mappie.api.ObjectMappie

object EmployeeRelationshipViewMapper : ObjectMappie<EmployeeRelationshipDo, EmployeeRelationshipVo>() {

    override fun map(from: EmployeeRelationshipDo): EmployeeRelationshipVo = mapping {
        // 手机号，身份证号脱敏
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
        to::phone fromExpression { DesensitizeUtils.desensitizePhone(from.phone) }
    }

}