package com.mao.auth.entity

/**
 * token响应结果
 */
data class TokenResponse(val accessToken: String, val refreshToken: String, val expiresIn: Long, val tokenType: String= "Bearer")