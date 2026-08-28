package com.mao.user.mapper

import com.mao.user.entity.UserProfileWorkDo
import com.mao.user.entity.UserProfileWorkVo
import tech.mappie.api.ObjectMappie

object UserProfileWorkViewMapper : ObjectMappie<UserProfileWorkDo, UserProfileWorkVo>() {

    override fun map(from: UserProfileWorkDo): UserProfileWorkVo = mapping {}

}