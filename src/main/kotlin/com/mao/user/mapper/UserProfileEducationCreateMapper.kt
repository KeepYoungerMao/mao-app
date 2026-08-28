package com.mao.user.mapper

import com.mao.user.entity.UserProfileEducationAddQo
import com.mao.user.entity.UserProfileEducationDo
import tech.mappie.api.ObjectMappie

object UserProfileEducationCreateMapper : ObjectMappie<UserProfileEducationAddQo, UserProfileEducationDo>() {

    override fun map(from: UserProfileEducationAddQo): UserProfileEducationDo = mapping {}

}