package com.mao.filter

import com.mao.entity.Operation
import com.mao.entity.OperationModule
import com.mao.entity.domain.OperationLogDo
import com.mao.extension.OperationLog
import com.mao.extension.OperationLogHandler
import com.mao.util.currentUser
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

@Component
class OperationLogFilter(
    private val operationLogHandler: OperationLogHandler
) : WebFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return mono {
            val startTime = System.currentTimeMillis()
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

    private suspend fun processOperationLog(exchange: ServerWebExchange, startTime: Long, errorMsg: String?) {
        // 1. 获取当前请求实际匹配的 Controller HandlerMethod (WebFlux核心特性)
        val handler = exchange.getAttribute<Any>(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE)
        if (handler !is HandlerMethod) return

        // 2. 解析注解 (先看方法，方法没有再看类)
        val methodAnn = handler.getMethodAnnotation(OperationLog::class.java)
        val classAnn = handler.beanType.getAnnotation(OperationLog::class.java)

        if (methodAnn == null) return

        val module = if (methodAnn.module != OperationModule.ERROR) {
            methodAnn.module
        } else if (classAnn.module != OperationModule.ERROR) {
            classAnn.module
        } else {
            return
        }
        val operation = if (methodAnn.operation != Operation.ERROR) methodAnn.operation else return
        val description = ""
        val username = currentUser() ?: "anonymous"
        val cost = System.currentTimeMillis() - startTime
        val ip = extractIp(exchange)
        val method = exchange.request.method.name()
        val operationLog = OperationLogDo(
            username = username,
            scope = module.scope.name,
            module = module.name,
            operation = operation.name,
            description = description,
            method = method,
            ip = ip,
            success = errorMsg == null,
            errorMessage = errorMsg,
            operationTime = startTime,
            cost = cost
        )
        operationLogHandler.sendLog(operationLog)
    }

    /**
     * 提取真实IP地址的辅助方法
     */
    private fun extractIp(exchange: ServerWebExchange): String {
        val headers = exchange.request.headers
        var ip = headers.getFirst("X-Forwarded-For")
        if (ip.isNullOrBlank() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("Proxy-Client-IP")
        }
        if (ip.isNullOrBlank() || "unknown".equals(ip, ignoreCase = true)) {
            ip = headers.getFirst("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrBlank() || "unknown".equals(ip, ignoreCase = true)) {
            ip = exchange.request.remoteAddress?.address?.hostAddress ?: "127.0.0.1"
        }
        // 如果经过多个反向代理，X-Forwarded-For 的第一个IP才是真实IP
        ip = ip.split(",")[0].trim()
        if (ip == "0:0:0:0:0:0:0:1") {
            ip = "127.0.0.1"
        }
        return ip
    }

}