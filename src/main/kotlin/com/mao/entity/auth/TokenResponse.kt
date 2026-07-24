package com.mao.entity.auth

data class TokenResponse(val accessToken: String, val refreshToken: String, val expiresIn: Long, val tokenType: String= "Bearer")
