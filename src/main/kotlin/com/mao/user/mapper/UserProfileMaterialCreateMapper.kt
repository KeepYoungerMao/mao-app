package com.mao.user.mapper

import com.mao.user.entity.UserProfileMaterialAddQo
import com.mao.user.entity.UserProfileMaterialDo
import tech.mappie.api.ObjectMappie

object UserProfileMaterialCreateMapper : ObjectMappie<UserProfileMaterialAddQo, UserProfileMaterialDo>() {

    override fun map(from: UserProfileMaterialAddQo): UserProfileMaterialDo = mapping {}

}