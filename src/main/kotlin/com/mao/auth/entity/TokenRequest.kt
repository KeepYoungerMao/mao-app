package com.mao.auth.entity

/**
 * 获取token请求参数
 */
data class TokenRequest(val username: String?, val password: String?, val timestamp: Long?)
