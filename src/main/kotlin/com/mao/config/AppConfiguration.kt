package com.mao.config

import com.mao.extension.CaffeineUserAuthCache
import com.mao.extension.GlobalResponseResultHandler
import com.mao.extension.UserAuthCache
import io.micrometer.context.ContextRegistry
import jakarta.annotation.PostConstruct
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer
import tools.jackson.databind.module.SimpleModule
import java.security.spec.MGF1ParameterSpec
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

// 日期format格式
const val DATE_FORMAT = "yyyy-MM-dd"
// 日期时间format格式
const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
// 默认时区东八区
val ZONE_SHANGHAI: ZoneId = ZoneId.of("Asia/Shanghai")
// 日期格式化器
val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZONE_SHANGHAI)
// 日期时间格式化器
val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT).withZone(ZONE_SHANGHAI)
// RSA算法
const val RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
// RSA加解密配置参数
val RSA_OAEP_SPEC: OAEPParameterSpec = OAEPParameterSpec(
    "SHA-256",
    "MGF1",
    MGF1ParameterSpec.SHA256,
    PSource.PSpecified.DEFAULT
)
// 全局日志名称
const val TRACE_ID = "traceId"

/**
 * 应用bean注册
 * 启用数据库审计功能：`@EnableR2dbcAuditing`
 */
@Configuration
@EnableR2dbcAuditing
class AppConfiguration {

    @PostConstruct
    fun appInit() {
        // 注册MDC中traceId的处理方式
        ContextRegistry.getInstance().registerThreadLocalAccessor(
            TRACE_ID,
            { MDC.get(TRACE_ID) },
            { value -> MDC.put(TRACE_ID, value) },
            { MDC.remove(TRACE_ID) },
        )
        // 开启Reactor的自动上下文传播
        Hooks.enableAutomaticContextPropagation()
    }

    /**
     * 全局响应结果包装处理器
     * 返回统一结构
     */
    @Bean
    fun responseResultHandler(serverCodecConfigurer: ServerCodecConfigurer,
                              resolver: RequestedContentTypeResolver,
                              adapterRegistry: ReactiveAdapterRegistry): GlobalResponseResultHandler {
        return GlobalResponseResultHandler(serverCodecConfigurer.writers, resolver, adapterRegistry)
    }

    /**
     * 注册Jackson序列化/反序列化类型转换
     * 支持将LocalDate、LocalDateTime在字符串之间进行互转
     */
    @Bean
    fun jsonMapperBuilderCustomizer(): JsonMapperBuilderCustomizer {
        return JsonMapperBuilderCustomizer { builder ->
            // 创建SimpleModule
            val timeModule = SimpleModule("CustomJavaTimeModule").apply {
                // 注册日期时间转换逻辑
                addSerializer(LocalDate::class.java, LocalDateSerializer(DATE_FORMATTER))
                addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DATETIME_FORMATTER))
                addDeserializer(LocalDate::class.java, LocalDateDeserializer(DATE_FORMATTER))
                addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DATETIME_FORMATTER))
            }
            // 注册
            builder.addModule(timeModule)
        }
    }

    /**
     * 审计功能中 创建用户、更新用户提供方式
     */
    @Bean
    fun auditorProvider(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware {
            ReactiveSecurityContextHolder.getContext()
                .mapNotNull { it.authentication }
                .filter { it.isAuthenticated }
                .map { authentication -> authentication.name }
                .switchIfEmpty(Mono.just("admin"))
        }
    }

    /**
     * 创建角色权限本地缓存
     */
    @Bean
    @ConditionalOnMissingClass("org.springframework.data.redis.connection.ReactiveRedisConnectionFactory")
    @ConditionalOnClass(name = ["com.github.benmanes.caffeine.cache.Caffeine"])
    fun localUserPermissionCache(jwtConfig: JwtConfig,
                                 reactiveUserDetailsService: ReactiveUserDetailsService) : UserAuthCache {
        return CaffeineUserAuthCache(jwtConfig, reactiveUserDetailsService)
    }

    /**
     * Spring Validation
     * 设置快速失败模式
     */
    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val validator = LocalValidatorFactoryBean()
        validator.validationPropertyMap["hibernate.validator.fail_fast"] = "true"
        return validator
    }

}