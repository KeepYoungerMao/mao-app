package com.mao.mapper

import com.mao.entity.UserProfileRelationshipAddQo
import com.mao.entity.UserProfileRelationshipDo
import com.mao.entity.UserProfileRelationshipUpdateQo
import com.mao.entity.UserProfileRelationshipVo
import com.mao.util.DesensitizeUtils
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserProfileRelationshipMapper {

    fun toVo(relationship: UserProfileRelationshipDo): UserProfileRelationshipVo =
        UserProfileRelationshipViewMapper.map(relationship)

    fun toDo(request: UserProfileRelationshipAddQo): UserProfileRelationshipDo =
        UserProfileRelationshipCreateMapper.map(request)

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

object UserProfileRelationshipViewMapper : ObjectMappie<UserProfileRelationshipDo, UserProfileRelationshipVo>() {
    override fun map(from: UserProfileRelationshipDo): UserProfileRelationshipVo = mapping {
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
        to::phone fromExpression { DesensitizeUtils.desensitizePhone(from.phone) }
    }
}

object UserProfileRelationshipCreateMapper :
    ObjectMappie<UserProfileRelationshipAddQo, UserProfileRelationshipDo>() {
    override fun map(from: UserProfileRelationshipAddQo): UserProfileRelationshipDo = mapping {}
}