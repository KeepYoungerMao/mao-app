package com.mao.user.entity

import jakarta.validation.constraints.NotBlank

/**
 * 更新用户密码请求参数类
 */
data class UserPasswordUpdateQo(
    @field:NotBlank
    val username: String? = null,
    @field:NotBlank
    val oldPassword: String? = null,
    @field:NotBlank
    val newPassword: String? = null,
    val timestamp: Long? = null
)
