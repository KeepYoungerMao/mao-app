package com.mao.user.mapper

import com.mao.user.entity.UserProfileWorkAddQo
import com.mao.user.entity.UserProfileWorkDo
import tech.mappie.api.ObjectMappie

object UserProfileWorkCreateMapper : ObjectMappie<UserProfileWorkAddQo, UserProfileWorkDo>() {

    override fun map(from: UserProfileWorkAddQo): UserProfileWorkDo = mapping {}

}