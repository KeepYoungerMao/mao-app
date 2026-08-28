package com.mao.user.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDateTime

/**
 * 用户查询结果包装类
 */
data class UserVo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expired: Boolean? = null,
    val locked: Boolean? = null,
    val enabled: Boolean? = null,
    val expireTime: LocalDateTime? = null,
    val lastLoginTime: LocalDateTime? = null
) : BaseVo()
