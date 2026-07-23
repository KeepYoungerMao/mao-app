package com.mao.entity.view

data class UserVo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expired: Boolean? = null,
    val locked: Boolean? = null,
    val enabled: Boolean? = null,
    val expireTime: Long? = null,
    val lastLoginTime: String? = null
) : BaseVo()