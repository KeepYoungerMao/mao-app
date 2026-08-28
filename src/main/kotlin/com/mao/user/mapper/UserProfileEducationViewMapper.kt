package com.mao.user.mapper

import com.mao.user.entity.UserProfileEducationDo
import com.mao.user.entity.UserProfileEducationVo
import tech.mappie.api.ObjectMappie

object UserProfileEducationViewMapper : ObjectMappie<UserProfileEducationDo, UserProfileEducationVo>() {

    override fun map(from: UserProfileEducationDo): UserProfileEducationVo = mapping {}

}