package com.mao.user.entity

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

/**
 * 更新用户上传材料请求参数类
 */
data class UserProfileMaterialUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Length(max = 50)
    val materialName: String? = null,
    @field:Length(max = 1000)
    val filePath: String? = null,
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
)
