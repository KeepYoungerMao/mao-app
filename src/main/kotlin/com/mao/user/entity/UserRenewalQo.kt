package com.mao.user.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * 用户续期请求参数类
 */
data class UserRenewalQo(
    @field:NotBlank
    val username: String? = null,
    @field:NotNull
    val expireTime: LocalDateTime? = null
)
