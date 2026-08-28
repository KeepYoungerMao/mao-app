package com.mao.user.mapper

import com.mao.common.util.DesensitizeUtils
import com.mao.user.entity.UserDo
import com.mao.user.entity.UserVo
import tech.mappie.api.ObjectMappie

object UserViewMapper: ObjectMappie<UserDo, UserVo>() {

    override fun map(from: UserDo): UserVo = mapping {
        // 手机号脱敏
        to::phone fromExpression  { DesensitizeUtils.desensitizePhone(from.phone) }
    }

}