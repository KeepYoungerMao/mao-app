package com.mao.util

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

/**
 * 获取当前认证用户信息
 */
suspend fun currentJwt() : Jwt? {
    val context = ReactiveSecurityContextHolder.getContext().awaitSingleOrNull()
    return context?.authentication?.principal as? Jwt
}

/**
 * 获取当前认证用户名称
 */
suspend fun currentUser() : String? = currentJwt()?.subject