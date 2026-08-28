package com.mao.common.filter

import com.mao.common.handler.OperationLog
import com.mao.common.handler.OperationLogHandler
import com.mao.log.entity.Operation
import com.mao.log.entity.OperationLogDo
import com.mao.log.entity.OperationModule
import com.mao.metric.service.ServerService
import com.mao.common.util.WebUtils
import com.mao.common.util.currentUser
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component
class OperationLogFilter(
    private val operationLogHandler: OperationLogHandler,
    private val serverService: ServerService
) : WebFilter, Ordered {

    private val annotationCache = ConcurrentHashMap<HandlerMethod, LogMeta>()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return mono {
            val startTime = LocalDateTime.now()
            var errorMsg: String? = null

            try {
                // 1. 挂起等待整个请求链路（Controller）执行完毕
                chain.filter(exchange).awaitSingleOrNull()
            } catch (e: Throwable) {
                errorMsg = e.message ?: "Unknown Error"
                throw e // 抛出异常，让全局异常处理器捕获
            } finally {
                // 2. 无论成功还是失败，在 finally 中记录日志
                processOperationLog(exchange, startTime, errorMsg)
            }
        }.then()
    }

    /**
     * 确保过滤器的优先级位于spring security之后
     */
    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE

    private suspend fun processOperationLog(exchange: ServerWebExchange, startTime: LocalDateTime, errorMsg: String?) {
        // 1. 获取当前请求实际匹配的 Controller HandlerMethod (WebFlux核心特性)
        val handler = exchange.getAttribute<Any>(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE)
        if (handler !is HandlerMethod) return

        // 2. 解析注解 (先看方法，方法没有再看类)
        val logMeta = annotationCache.computeIfAbsent(handler) { resolveLogMeta(it) }
        if (!logMeta.requiresLogging) return

        val description = ""
        val username = currentUser() ?: "anonymous"
        val cost = Duration.between(startTime, LocalDateTime.now()).toMillis()
        val ip = WebUtils.getIp(exchange)
        val method = exchange.request.method.name()
        val operationLog = OperationLogDo(
            username = username,
            scope = logMeta.module!!.scope.name,
            module = logMeta.module.name,
            operation = logMeta.operation!!.name,
            description = description,
            method = method,
            ip = ip,
            success = errorMsg == null,
            errorMessage = errorMsg,
            operationTime = startTime,
            cost = cost
        )
        // 记录交易日志
        operationLogHandler.sendLog(operationLog)
        // 记录系统指标
        serverService.recordRequest(cost, errorMsg == null)
    }

    /**
     * 解析 HandlerMethod 上的注解信息并封装为 LogMeta
     */
    private fun resolveLogMeta(handler: HandlerMethod): LogMeta {
        val methodAnn = handler.getMethodAnnotation(OperationLog::class.java)
            ?: return LogMeta.NOT_REQUIRED

        val classAnn = handler.beanType.getAnnotation(OperationLog::class.java)

        // 提取 Module，优先使用方法上的，没有再看类上的
        val module = if (methodAnn.module != OperationModule.UNSET) {
            methodAnn.module
        } else if (classAnn != null && classAnn.module != OperationModule.UNSET) {
            classAnn.module
        } else {
            return LogMeta.NOT_REQUIRED
        }

        // 提取 Operation，必须在方法上显式指定
        val operation = if (methodAnn.operation != Operation.UNSET) {
            methodAnn.operation
        } else {
            return LogMeta.NOT_REQUIRED
        }

        return LogMeta(requiresLogging = true, module = module, operation = operation)
    }

    /**
     * 内部缓存数据结构
     */
    private class LogMeta(
        val requiresLogging: Boolean,
        val module: OperationModule? = null,
        val operation: Operation? = null
    ) {
        companion object {
            // 单例对象，用于负面缓存（表示该方法不需要记录日志）
            val NOT_REQUIRED = LogMeta(requiresLogging = false)
        }
    }

}