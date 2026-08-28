package com.mao.common.handler

import com.mao.log.entity.Operation
import com.mao.log.entity.OperationModule

/**
 * 操作日志
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OperationLog(
    val module: OperationModule = OperationModule.UNSET,
    val operation: Operation = Operation.UNSET
)
