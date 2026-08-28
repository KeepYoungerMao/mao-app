package com.mao.common.listener

/**
 * 用户创建成功后事件
 */
data class UserCreateEvent(val name: String, val email: String, val password: String)
