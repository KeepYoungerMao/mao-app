package com.mao.common.util

import org.springframework.web.server.ServerWebExchange

object WebUtils {

    /**
     * 从HTTP headers中获取用户IP地址
     */
    @JvmStatic
    fun getIp(exchange: ServerWebExchange): String {
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