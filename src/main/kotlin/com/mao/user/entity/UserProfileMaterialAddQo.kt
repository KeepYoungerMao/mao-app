package com.mao.user.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

/**
 * 新增用户上传材料请求参数类
 */
data class UserProfileMaterialAddQo(
    @field:NotNull
    val userId: Int? = null,
    @field:NotBlank
    @field:Length(max = 50)
    val materialName: String? = null,
    @field:NotBlank
    @field:Length(max = 1000)
    val filePath: String? = null,
    @field:NotNull
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
)
