package com.mao.user.mapper

import com.mao.common.util.DesensitizeUtils
import com.mao.user.entity.UserProfileRelationshipDo
import com.mao.user.entity.UserProfileRelationshipVo
import tech.mappie.api.ObjectMappie

object UserProfileRelationshipViewMapper : ObjectMappie<UserProfileRelationshipDo, UserProfileRelationshipVo>() {

    override fun map(from: UserProfileRelationshipDo): UserProfileRelationshipVo = mapping {
        // 手机号，身份证号脱敏
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
        to::phone fromExpression { DesensitizeUtils.desensitizePhone(from.phone) }
    }

}