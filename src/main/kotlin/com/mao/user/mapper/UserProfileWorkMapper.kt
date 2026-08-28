package com.mao.user.mapper

import com.mao.user.entity.UserProfileWorkDo
import com.mao.user.entity.UserProfileWorkUpdateQo

object UserProfileWorkMapper {

    fun copyToExistDo(request: UserProfileWorkUpdateQo, work: UserProfileWorkDo): UserProfileWorkDo = work.apply {
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