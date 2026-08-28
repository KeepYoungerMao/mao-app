package com.mao.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "app.jwt")
data class JwtConfig(

    // 私钥
    val privateKey: Resource,

    // 公钥
    val publicKey: Resource,

    // token过期时间 ms
    val accessTokenExpiration: Long = 3600000,

    // refresh token过期时间 ms
    val refreshTokenExpiration: Long = 604800000,

    // 是否开启重放攻击检测
    val replayAttackCheck: Boolean = true,

    // 重放攻击检测时间
    val replayAttackTime: Int = 30000,

)
