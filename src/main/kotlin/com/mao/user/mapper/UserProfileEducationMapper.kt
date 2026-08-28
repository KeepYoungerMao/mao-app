package com.mao.user.mapper

import com.mao.user.entity.UserProfileEducationDo
import com.mao.user.entity.UserProfileEducationUpdateQo
import org.springframework.stereotype.Component

object UserProfileEducationMapper {

    fun copyToExistDo(request: UserProfileEducationUpdateQo, education: UserProfileEducationDo): UserProfileEducationDo = education.apply {
        request.institutionName?.let { institutionName = it }
        request.degree?.let { degree = it }
        request.major?.let { major = it }
        request.startDate?.let { startDate = it }
        request.endDate?.let { endDate = it }
        request.additionalInfo?.let { additionalInfo = it }
    }

}