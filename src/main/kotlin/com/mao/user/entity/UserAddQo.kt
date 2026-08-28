package com.mao.user.entity

import com.mao.common.validate.IdCard
import com.mao.common.validate.Phone
import com.mao.common.validate.Username
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 新增用户请求参数类
 */
data class UserAddQo(
    @field:NotBlank
    @field:Username
    val username: String? = null,
    @field:NotBlank
    @field:Length(max = 300)
    val avatar: String? = null,
    @field:NotBlank
    @field:Phone
    val phone: String? = null,
    @field:NotBlank
    @field:Length(max = 32)
    @field:Email
    val email: String? = null,
    @field:NotNull
    val expireTime: LocalDateTime? = null,
    @field:NotBlank
    @field:Length(min = 2, max = 20)
    val realName: String? = null,
    @field:NotNull
    val entryDate: LocalDate? = null,
    @field:NotBlank
    @field:IdCard
    val idCardNum: String? = null,
    @field:NotNull
    val birthday: LocalDate? = null
)
