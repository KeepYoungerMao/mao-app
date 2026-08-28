package com.mao.user.mapper

import com.mao.common.util.DesensitizeUtils
import com.mao.user.entity.UserProfileDo
import com.mao.user.entity.UserProfileVo
import tech.mappie.api.ObjectMappie

object UserProfileViewMapper : ObjectMappie<UserProfileDo, UserProfileVo>() {

    override fun map(from: UserProfileDo): UserProfileVo = mapping {
        // 身份证号脱敏
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
    }

}