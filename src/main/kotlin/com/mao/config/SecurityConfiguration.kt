package com.mao.config

import com.mao.extension.CustomAuthHandler
import com.mao.extension.RolePermissionData
import com.mao.extension.UserRolePermissionCache
import com.mao.util.RsaUtils
import com.nimbusds.jose.jwk.RSAKey
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Flux
import java.util.*

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(JwtConfig::class)
class SecurityConfiguration(
    private val jwtConfig: JwtConfig,
    private val authHandler: CustomAuthHandler
) {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity,
                            reactiveJwtDecoder: ReactiveJwtDecoder,
                            reactiveJwtAuthenticationConverter: ReactiveJwtAuthenticationConverter
    ): SecurityWebFilterChain {

        return http {
            // 跨站请求伪造 关闭
            csrf { disable() }
            // 无状态会话
            securityContextRepository = NoOpServerSecurityContextRepository.getInstance()
            // 接口授权
            authorizeExchange {
                authorize("/api/v1/auth/**", permitAll)
                authorize(anyExchange, authenticated)
            }
            // oauth2
            oauth2ResourceServer {
                jwt {
                    jwtDecoder = reactiveJwtDecoder
                    jwtAuthenticationConverter = reactiveJwtAuthenticationConverter
                }
                authenticationEntryPoint = authHandler
                accessDeniedHandler = authHandler
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * JWT解析
     * 注册公钥数据
     */
    @Bean
    fun reactiveJwtDecoder(rsaKey: RSAKey): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build()
    }

    /**
     * 自定义权限转换器
     * 默认ReactiveJwtAuthenticationConverter直接洗scp/scope范围权限，一般为 read write admin
     * 我们要存储自定义权限不会进行解析
     */
    @Bean
    fun reactiveJwtAuthenticationConverter(userRolePermissionCache: UserRolePermissionCache): ReactiveJwtAuthenticationConverter {
        val converter = ReactiveJwtAuthenticationConverter()
        // 根据username实时查询权限信息
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            mono {
                userRolePermissionCache.get(jwt.subject) ?: RolePermissionData("", emptyList(), emptyList())
            }.flatMapMany { permissionData ->
                val authorities = mutableSetOf<GrantedAuthority>()
                permissionData.roles.forEach { authorities.add(SimpleGrantedAuthority("ROLE_$it")) }
                permissionData.permissions.forEach { authorities.add(SimpleGrantedAuthority(it)) }
                Flux.fromIterable(authorities)
            }
        }
        return converter
    }

    @Bean
    fun rsaKey(): RSAKey {
        val publicKey = RsaUtils.parsePublicKey(jwtConfig.publicKey)
        val privateKey = RsaUtils.parsePrivateKey(jwtConfig.privateKey)
        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

}