package com.mao.config

import com.mao.extension.CustomAuthHandler
import com.mao.util.RsaUtils
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
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
                            jwtDecoder: ReactiveJwtDecoder,
                            jwtAuthenticationConverter: ReactiveJwtAuthenticationConverter): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange {
                exchanges -> exchanges.pathMatchers("/api/v1/auth/**").permitAll()
                    .anyExchange().authenticated()
            }.oauth2ResourceServer {
                oauth2 -> oauth2.jwt {
                    jwt -> jwt.jwtDecoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
                oauth2.authenticationEntryPoint(authHandler)
                oauth2.accessDeniedHandler(authHandler)
            }
            .build()
    }

    @Bean
    fun reactiveJwtDecoder(rsaKey: RSAKey): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtAuthenticationConverter(): ReactiveJwtAuthenticationConverter {
        val converter = ReactiveJwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val authorities = mutableSetOf<GrantedAuthority>()
            // TODO 添加权限列表
            authorities.add(SimpleGrantedAuthority("ROLE_admin"))
            Flux.fromIterable(authorities)
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