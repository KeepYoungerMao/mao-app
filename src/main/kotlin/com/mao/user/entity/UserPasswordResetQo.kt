package com.mao.user.entity

import jakarta.validation.constraints.NotBlank

/**
 * 重置用户密码请求参数类
 */
data class UserPasswordResetQo(
    @field:NotBlank
    val username: String? = null
)
