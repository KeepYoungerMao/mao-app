package com.mao.mapper

import com.mao.entity.UserProfileEducationAddQo
import com.mao.entity.UserProfileEducationDo
import com.mao.entity.UserProfileEducationUpdateQo
import com.mao.entity.UserProfileEducationVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserProfileEducationMapper {

    fun toVo(education: UserProfileEducationDo): UserProfileEducationVo =
        UserProfileEducationViewMapper.map(education)

    fun toDo(request: UserProfileEducationAddQo): UserProfileEducationDo =
        UserProfileEducationCreateMapper.map(request)

    fun copyToExistDo(
        request: UserProfileEducationUpdateQo,
        education: UserProfileEducationDo
    ): UserProfileEducationDo = education.apply {
        request.institutionName?.let { institutionName = it }
        request.degree?.let { degree = it }
        request.major?.let { major = it }
        request.startDate?.let { startDate = it }
        request.endDate?.let { endDate = it }
        request.additionalInfo?.let { additionalInfo = it }
    }
}

object UserProfileEducationViewMapper : ObjectMappie<UserProfileEducationDo, UserProfileEducationVo>() {
    override fun map(from: UserProfileEducationDo): UserProfileEducationVo = mapping {}
}

object UserProfileEducationCreateMapper : ObjectMappie<UserProfileEducationAddQo, UserProfileEducationDo>() {
    override fun map(from: UserProfileEducationAddQo): UserProfileEducationDo = mapping {}
}