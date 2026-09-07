package com.mao.employee.mapper

import com.mao.common.util.DesensitizeUtils
import com.mao.employee.entity.EmployeeDo
import com.mao.employee.entity.EmployeeVo
import tech.mappie.api.ObjectMappie

object EmployeeViewMapper : ObjectMappie<EmployeeDo, EmployeeVo>() {

    override fun map(from: EmployeeDo): EmployeeVo = mapping {
        // 身份证号脱敏
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
    }

}