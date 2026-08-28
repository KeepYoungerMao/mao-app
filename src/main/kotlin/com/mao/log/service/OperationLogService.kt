package com.mao.log.service

import com.mao.log.repository.OperationLogRepository
import org.springframework.stereotype.Service

@Service
class OperationLogService(
    private val operationLogRepository: OperationLogRepository
) {
}