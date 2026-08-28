package com.mao.user.entity

import com.mao.common.validate.Phone
import com.mao.common.validate.Username
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * 更新用户请求参数类
 */
data class UserUpdateQo(
    @field:NotNull
    val id: Int? = null,
    @field:Username
    val username: String? = null,
    @field:Length(max = 300)
    val avatar: String? = null,
    @field:Phone
    val phone: String? = null,
    @field:Email
    val email: String? = null
)
