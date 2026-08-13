package com.mao.extension

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

/**
 * 一个时间分钟内的请求统计。
 *
 * 例如：
 * 2026-08-12 17:49:00 ~ 17:49:59
 */
class MinuteServerMetricBucket(
    val minute: LocalDateTime,
) {

    private val totalRequests = AtomicLong(0)
    private val successRequests = AtomicLong(0)
    private val errorRequests = AtomicLong(0)
    private val totalResponseTimeMillis = AtomicLong(0)

    fun record(responseTimeMillis: Long, success: Boolean) {
        totalRequests.incrementAndGet()
        if (success) {
            successRequests.incrementAndGet()
        } else {
            errorRequests.incrementAndGet()
        }
        totalResponseTimeMillis.addAndGet(responseTimeMillis)
    }

    fun snapshot(): MinuteServerMetricSnapshot {
        val total = totalRequests.get()
        val totalResponseTime = totalResponseTimeMillis.get()
        return MinuteServerMetricSnapshot(
            minute = minute,
            totalRequests = total,
            successRequests = successRequests.get(),
            errorRequests = errorRequests.get(),
            totalResponseTimeMillis = totalResponseTime,
            avgResponseTimeMillis = if (total == 0L) { 0L } else { totalResponseTime / total },
        )
    }
}