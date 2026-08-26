package com.mao.mapper

import com.mao.entity.UserProfileMaterialAddQo
import com.mao.entity.UserProfileMaterialDo
import com.mao.entity.UserProfileMaterialUpdateQo
import com.mao.entity.UserProfileMaterialVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserProfileMaterialMapper {

    fun toVo(material: UserProfileMaterialDo): UserProfileMaterialVo = UserProfileMaterialViewMapper.map(material)

    fun toDo(request: UserProfileMaterialAddQo): UserProfileMaterialDo = UserProfileMaterialCreateMapper.map(request)

    fun copyToExistDo(
        request: UserProfileMaterialUpdateQo,
        material: UserProfileMaterialDo
    ): UserProfileMaterialDo = material.apply {
        request.materialName?.let { materialName = it }
        request.filePath?.let { filePath = it }
        request.uploadTime?.let { uploadTime = it }
        request.description?.let { description = it }
    }
}

object UserProfileMaterialViewMapper : ObjectMappie<UserProfileMaterialDo, UserProfileMaterialVo>() {
    override fun map(from: UserProfileMaterialDo): UserProfileMaterialVo = mapping {}
}

object UserProfileMaterialCreateMapper : ObjectMappie<UserProfileMaterialAddQo, UserProfileMaterialDo>() {
    override fun map(from: UserProfileMaterialAddQo): UserProfileMaterialDo = mapping {}
}