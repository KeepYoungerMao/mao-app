package com.mao.config

import com.mao.extension.GlobalResponseResultHandler
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import reactor.core.publisher.Mono
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer
import tools.jackson.databind.module.SimpleModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


const val DATE_FORMAT = "yyyy-MM-dd"
const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
val ZONE_SHANGHAI: ZoneId = ZoneId.of("Asia/Shanghai")
val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZONE_SHANGHAI)
val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT).withZone(ZONE_SHANGHAI)

/**
 * 应用bean注册
 * 启用数据库审计功能
 */
@Configuration
@EnableR2dbcAuditing
class AppConfiguration {

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
            Mono.just("admin")
        }
    }

}