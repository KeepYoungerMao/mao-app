package com.mao.listener

/**
 * 用户创建成功后事件
 */
data class UserCreateEvent(val email: String, val password: String)
