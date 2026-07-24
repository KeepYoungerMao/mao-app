package com.mao.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "app.jwt")
data class JwtConfig(
    val privateKey: Resource,
    val publicKey: Resource,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,
)
