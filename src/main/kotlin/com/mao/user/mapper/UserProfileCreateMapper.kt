package com.mao.user.mapper

import com.mao.user.entity.UserAddQo
import com.mao.user.entity.UserProfileDo
import tech.mappie.api.ObjectMappie

object UserProfileCreateMapper : ObjectMappie<UserAddQo, UserProfileDo>() {

    override fun map(from: UserAddQo): UserProfileDo = mapping {}

}