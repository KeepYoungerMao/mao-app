package com.mao.metric.entity

import java.time.LocalDateTime

data class MinuteServerMetricSnapshot(
    val minute: LocalDateTime,
    val totalRequests: Long,
    val successRequests: Long,
    val errorRequests: Long,
    val totalResponseTimeMillis: Long,
    val avgResponseTimeMillis: Long,
)
