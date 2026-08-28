package com.mao.user.mapper

import com.mao.user.entity.UserProfileMaterialDo
import com.mao.user.entity.UserProfileMaterialVo
import tech.mappie.api.ObjectMappie

object UserProfileMaterialViewMapper : ObjectMappie<UserProfileMaterialDo, UserProfileMaterialVo>() {

    override fun map(from: UserProfileMaterialDo): UserProfileMaterialVo = mapping {}

}