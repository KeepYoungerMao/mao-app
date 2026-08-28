package com.mao.metric.entity

import java.time.LocalDateTime

/**
 * 服务器信息实体类
 */
data class ServerInfo(
    val serverName: String,
    val version: String,
    val startTime: LocalDateTime,
    val liveTime: String,
    val totalRequests: Long,
    val successResponse: Long,
    val errorResponse: Long,
    val avgResponse: Long,
    val onlineUsers: Int,
    val loginUsers: Int
)
