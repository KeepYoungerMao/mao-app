package com.mao.entity.query

data class UserUpdateQo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expireTime: Long? = null,
)
