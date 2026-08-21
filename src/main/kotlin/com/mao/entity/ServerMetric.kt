package com.mao.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_server_metric")
data class ServerMetric(
    @Id val id: Long,
    val minuteStart: LocalDateTime,
    val totalRequests: Long,
    val successRequests: Long,
    val errorRequests: Long,
    val totalResponseTimeMillis: Long,
    val avgResponseTimeMillis: Long,
    val onlineUsers: Int,
    val loginUsers: Int,
    val createdTime: LocalDateTime,
    @Transient @get:JvmName("getIsNewRecord") private val isNewRecord: Boolean = false
) : Persistable<Long> {
    override fun getId(): Long = id
    override fun isNew(): Boolean = isNewRecord
}

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
