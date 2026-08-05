package com.mao.extension

import com.mao.entity.domain.OperationLogDo
import com.mao.repository.OperationLogRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper

/**
 * 操作日志处理器
 */
@Service
class OperationLogHandler(
    private val operationLogRepository: OperationLogRepository,
    private val jsonMapper: JsonMapper
) {

    private val log = LoggerFactory.getLogger(javaClass)

    // 设置容量为5000，当消费不了，将采用日志打印方式记录操作日志
    private val channel = Channel<OperationLogDo>(capacity = 5000)

    @PostConstruct
    fun init() {
        // 启动一个独立的后台协程，专职负责写库
        // SupervisorJob 确保单条日志写库报错时，整个消费协程不会崩溃
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            for (operationLog in channel) {
                try {
                    operationLogRepository.save(operationLog)
                } catch (e: Exception) {
                    // 这里可扩展: 发生数据库宕机时，可将日志写入本地磁盘 fallback
                    log.error("操作日志保存失败: ", e)
                    log.error("-----------------------")
                    log.error("[operation log]: {}", jsonMapper.writeValueAsString(operationLog))
                    log.error("-----------------------")
                }
            }
        }
    }

    /**
     * 提供给外部发送日志的方法 (极其轻量，不会阻塞)
     */
    fun sendLog(operationLog: OperationLogDo) {
        channel.trySend(operationLog)
    }

}