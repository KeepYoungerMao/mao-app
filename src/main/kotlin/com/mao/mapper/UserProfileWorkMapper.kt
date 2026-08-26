package com.mao.mapper

import com.mao.entity.UserProfileWorkAddQo
import com.mao.entity.UserProfileWorkDo
import com.mao.entity.UserProfileWorkUpdateQo
import com.mao.entity.UserProfileWorkVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserProfileWorkMapper {

    fun toVo(work: UserProfileWorkDo): UserProfileWorkVo = UserProfileWorkViewMapper.map(work)

    fun toDo(request: UserProfileWorkAddQo): UserProfileWorkDo = UserProfileWorkCreateMapper.map(request)

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

object UserProfileWorkViewMapper : ObjectMappie<UserProfileWorkDo, UserProfileWorkVo>() {
    override fun map(from: UserProfileWorkDo): UserProfileWorkVo = mapping {}
}

object UserProfileWorkCreateMapper : ObjectMappie<UserProfileWorkAddQo, UserProfileWorkDo>() {
    override fun map(from: UserProfileWorkAddQo): UserProfileWorkDo = mapping {}
}