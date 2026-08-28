package com.mao.user.mapper

import com.mao.user.entity.UserProfileRelationshipAddQo
import com.mao.user.entity.UserProfileRelationshipDo
import tech.mappie.api.ObjectMappie

object UserProfileRelationshipCreateMapper : ObjectMappie<UserProfileRelationshipAddQo, UserProfileRelationshipDo>() {

    override fun map(from: UserProfileRelationshipAddQo): UserProfileRelationshipDo = mapping {}

}