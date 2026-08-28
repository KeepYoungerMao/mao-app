package com.mao.log.controller

import com.mao.common.handler.OperationLog
import com.mao.log.entity.OperationModule
import com.mao.log.service.OperationLogService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/operation-log")
@OperationLog(OperationModule.OPERATION_LOG)
class OperationLogController (
    private val operationLogService: OperationLogService
) {
}