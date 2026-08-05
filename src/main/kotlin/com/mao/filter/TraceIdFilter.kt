package com.mao.filter

import com.mao.util.RandomUtils
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class TraceIdFilter: WebFilter {

    companion object {
        // W3C Trace Context 国际标准 (格式: 00-traceId-spanId-flags)
        const val W3C_TRACE_ID = "traceparent"
        // Zipkin / Brave 标准 (B3 协议)
        const val B3_TRACE_ID = "X-B3-TraceId"
        // Envoy / Nginx 常用唯一请求 ID 标准
        const val REQUEST_ID = "X-Request-Id"
        // 常见的自定义标准
        const val CUSTOM_TRACE_ID = "X-Trace-Id"
        // 系统内部使用名称
        const val TRACE_ID = "traceId"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val traceId = extractTraceId(exchange) ?: RandomUtils.numbers(20)
        return chain.filter(exchange).contextWrite { context -> context.put(TRACE_ID,traceId) }
    }

    /**
     * 从header中获取traceId
     */
    private fun extractTraceId(exchange: ServerWebExchange): String? {
        val headers = exchange.request.headers

        headers.getFirst(W3C_TRACE_ID)?.let { traceparent ->
            val parts = traceparent.split("-")
            if (parts.size >= 2 && parts[1].isNotBlank()) {
                return parts[1] // 只取中间真正的 traceId 部分
            }
        }
        headers.getFirst(B3_TRACE_ID)?.let { if (it.isNotBlank()) return it }
        headers.getFirst(REQUEST_ID)?.let { if (it.isNotBlank()) return it }
        headers.getFirst(CUSTOM_TRACE_ID)?.let { if (it.isNotBlank()) return it }
        headers.getFirst(TRACE_ID)?.let { if (it.isNotBlank()) return it }

        return null
    }

}