package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeWorkDo
import com.mao.employee.entity.EmployeeWorkUpdateQo

object EmployeeWorkMapper {

    fun copyToExistDo(request: EmployeeWorkUpdateQo, work: EmployeeWorkDo): EmployeeWorkDo = work.apply {
        request.companyName?.let { companyName = it }
        request.jobTitle?.let { jobTitle = it }
        request.industry?.let { industry = it }
        request.industryId?.let { industryId = it }
        request.startDate?.let { startDate = it }
        request.endDate?.let { endDate = it }
        request.responsibilities?.let { responsibilities = it }
        request.currentEmployment?.let { currentEmployment = it }
    }

}