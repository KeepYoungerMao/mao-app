package com.mao.config

import com.mao.entity.ServerMetric
import com.mao.extension.MinuteServerMetricSnapshot
import com.mao.repository.ServerMetricRepository
import com.mao.service.SystemService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 定时任务管理器
 */
@Configuration
@EnableScheduling
class ScheduleConfiguration(
    private val serverMetricRepository: ServerMetricRepository,
    private val systemService: SystemService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 服务指标数据队列
     * 应对数据库不稳定时指标数据保存失败的问题。
     * 该方式不能阻止JVM崩溃场景的数据丢失问题。
     */
    private val metricQueue = ConcurrentLinkedQueue<MinuteServerMetricSnapshot>()

    /**
     * 保证服务指标数据只有一个任务在执行
     */
    private val isServerMetricRunning = AtomicBoolean(false)

    /**
     * 每分钟保存一条服务运行指标数据
     */
    @Scheduled(cron = "0 * * * * *")
    fun persistCurrentMinuteServerMetrics() = runBlocking {
        if (!isServerMetricRunning.compareAndSet(false, true)) {
            log.warn("Previous server metric persistence is still running, skip this execution")
            return@runBlocking
        }
        try {
            log.info("Persisting current minute metrics")
            // 取老桶
            val oldBucket = systemService.rotateMinuteBucket()
            // 获取桶里数据
            val snapshot = oldBucket.snapshot()
            // 放入队列
            metricQueue.offer(snapshot)
            // 执行一次保存
            persistMetrics()
        } finally {
            isServerMetricRunning.set(false)
        }
    }

    private suspend fun persistMetrics() {
        while (metricQueue.isNotEmpty()) {
            val metric = metricQueue.peek() ?: return
            try {
                saveMinuteServerMetricSnapshot(metric)
                metricQueue.poll()
            } catch (e: Exception) {
                log.error("Error in persisting metrics", e)
                return
            }
        }
    }

    private suspend fun saveMinuteServerMetricSnapshot(snapshot: MinuteServerMetricSnapshot) {
        val now = LocalDateTime.now()
        val id = snapshot.minute.atZone(ZoneId.systemDefault()).toInstant().epochSecond
        serverMetricRepository.save(
            ServerMetric(
                id = id,
                minuteStart = snapshot.minute,
                totalRequests = snapshot.totalRequests,
                successRequests = snapshot.successRequests,
                errorRequests = snapshot.errorRequests,
                totalResponseTimeMillis = snapshot.totalResponseTimeMillis,
                avgResponseTimeMillis = snapshot.avgResponseTimeMillis,
                onlineUsers = systemService.getCurrentOnlineUser(),
                loginUsers = systemService.getTodayLoginUserCount(),
                createdTime = now,
                isNewRecord = true
            )
        )
    }

}