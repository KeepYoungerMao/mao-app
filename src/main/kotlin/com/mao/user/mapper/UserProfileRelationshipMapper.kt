package com.mao.user.mapper

import com.mao.user.entity.UserProfileRelationshipDo
import com.mao.user.entity.UserProfileRelationshipUpdateQo

object UserProfileRelationshipMapper {

    fun copyToExistDo(
        request: UserProfileRelationshipUpdateQo,
        relationship: UserProfileRelationshipDo
    ): UserProfileRelationshipDo = relationship.apply {
        request.realName?.let { realName = it }
        request.relationshipId?.let { relationshipId = it }
        request.idCardNum?.let { idCardNum = it }
        request.phone?.let { phone = it }
        request.remark?.let { remark = it }
    }

}