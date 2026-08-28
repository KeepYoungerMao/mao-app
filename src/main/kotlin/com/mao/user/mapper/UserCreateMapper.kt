package com.mao.user.mapper

import com.mao.user.entity.UserAddQo
import com.mao.user.entity.UserDo
import tech.mappie.api.ObjectMappie

object UserCreateMapper: ObjectMappie<UserAddQo, UserDo>() {

    override fun map(from: UserAddQo): UserDo = mapping {}

}