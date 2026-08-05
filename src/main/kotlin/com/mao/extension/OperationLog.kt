package com.mao.extension

import com.mao.entity.Operation
import com.mao.entity.OperationModule

/**
 * 操作日志
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OperationLog(
    val module: OperationModule = OperationModule.ERROR,
    val operation: Operation = Operation.ERROR
)
