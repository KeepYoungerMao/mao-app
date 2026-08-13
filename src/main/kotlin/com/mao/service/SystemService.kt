package com.mao.service

import com.mao.entity.ServerInfo
import com.mao.extension.MinuteServerMetricBucket
import com.mao.extension.UserRolePermissionCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/**
 * ## 系统服务处理
 */
@Service
class SystemService(
    @Value("\${spring.application.name}")
    private val serverName: String,
    private val buildProperties: BuildProperties,
    private val userRolePermissionCache: UserRolePermissionCache
) {

    /**
     * 程序启动时间
     */
    private val startTime: Instant = Instant.now()

    /**
     * 总请求数
     */
    private val totalRequests = AtomicLong(0)

    /**
     * 成功请求数
     */
    private val successRequests = AtomicLong(0)

    /**
     * 失败请求数
     */
    private val errorRequests = AtomicLong(0)

    /**
     * 所有请求耗时总和，单位：毫秒
     * 用 AtomicLong 做累计，计算平均响应时间时：
     * totalResponseTime / totalRequests
     */
    private val totalResponseTimeMillis = AtomicLong(0)

    /**
     * 当前分钟统计桶
     * 所有 HTTP 请求都会写入这里。
     * 分钟切换时使用 getAndSet() 原子替换。
     */
    private val currentMinuteBucket = AtomicReference(createCurrentMinuteBucket())

    /**
     * 当前统计登录用户的日期
     */
    private val loginDate = AtomicReference(LocalDate.now())

    /**
     * 今日登录过的用户
     */
    private val todayLoginUsers = ConcurrentHashMap.newKeySet<Int>()

    /**
     * 记录一次 HTTP 请求。
     * 每个请求完成后调用一次。
     */
    fun recordRequest(responseTimeMillis: Long, success: Boolean) {
        // 记录程序启动以来的累计数据
        totalRequests.incrementAndGet()
        if (success) {
            successRequests.incrementAndGet()
        } else {
            errorRequests.incrementAndGet()
        }
        totalResponseTimeMillis.addAndGet(responseTimeMillis)
        // 记录当前分钟数据，即使此时刚刚发生分钟切换，这里拿到的也一定是某一个完整的 Bucket。
        currentMinuteBucket.get().record(responseTimeMillis = responseTimeMillis, success = success)
    }

    /**
     * 记录用户登录。
     *
     * 同一个用户当天重复登录，只统计一次。
     */
    fun recordLogin(userId: Int) {
        resetLoginStatisticsIfNeeded()
        todayLoginUsers.add(userId)
    }

    /**
     * 获取系统信息。
     */
    fun getServerInfo(): ServerInfo {
        resetLoginStatisticsIfNeeded()
        val total = totalRequests.get()
        val averageResponse = if (total == 0L) { 0L } else { totalResponseTimeMillis.get() / total }
        return ServerInfo(
            serverName = serverName,
            version = buildProperties.version ?: "no version",
            startTime = startTime.atZone(ZoneId.systemDefault()).toLocalDateTime(),
            liveTime = formatLiveTime(),
            totalRequests = total,
            successResponse = successRequests.get(),
            errorResponse = errorRequests.get(),
            avgResponse = averageResponse,
            onlineUsers = userRolePermissionCache.getTotal(),
            loginUsers = todayLoginUsers.size,
        )
    }

    /**
     * ## 获取当前在线人数
     * UserRolePermission类缓存了获取token用户的角色和权限信息，
     * 通过获取缓存的个数，可以大体反应当前在线人数，误差30分钟（取决于token有效期）
     */
    fun getCurrentOnlineUser(): Int = userRolePermissionCache.getTotal()

    /**
     * ## 转换桶
     * 将当前缓存的桶数据返回，并为缓存设置一个新桶，原子操作。
     */
    fun rotateMinuteBucket(): MinuteServerMetricBucket {
        return currentMinuteBucket.getAndSet(createCurrentMinuteBucket())
    }

    /**
     * ## 创建一个新桶
     * 创建当前时间分钟数的桶
     */
    private fun createCurrentMinuteBucket(): MinuteServerMetricBucket {
        val minute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        return MinuteServerMetricBucket(minute)
    }

    fun getTodayLoginUserCount(): Int {
        resetLoginStatisticsIfNeeded()
        return todayLoginUsers.size
    }

    /**
     * ## 重设登录统计信息
     * 如果到了第二天，将清空统计。
     * 该方法被 getTodayLoginUserCount() 方法调用，继而被每分钟执行一次的定时任务调用，
     * 因此第二天的统计信息一定会被清空。
     */
    private fun resetLoginStatisticsIfNeeded() {
        val today = LocalDate.now()
        val currentDate = loginDate.get()
        if (currentDate == today) {
            return
        }
        /**
         * CAS 保证并发情况下只有一个线程负责切换日期。
         */
        if (loginDate.compareAndSet(currentDate, today)) {
            todayLoginUsers.clear()
        }
    }

    /**
     * ## 格式化服务存活时间
     * 格式为：?d ?h ?m ?s
     */
    private fun formatLiveTime(): String {
        val duration = Duration.between(
            startTime,
            Instant.now(),
        )
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        return buildString {
            if (days > 0) {
                append(days)
                append("d ")
            }
            if (hours > 0) {
                append(hours)
                append("h ")
            }
            if (minutes > 0) {
                append(minutes)
                append("m ")
            }
            append(seconds)
            append("s")
        }
    }

}