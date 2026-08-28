package com.mao.user.mapper

import com.mao.user.entity.UserProfileMaterialDo
import com.mao.user.entity.UserProfileMaterialUpdateQo
import org.springframework.stereotype.Component

object UserProfileMaterialMapper {

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