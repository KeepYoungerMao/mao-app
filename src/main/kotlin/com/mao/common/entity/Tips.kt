package com.mao.common.entity

import java.time.LocalDateTime

/**
 * 提示信息包装类
 * 用于操作类接口无实际返回数据时，返回操作结果提示信息
 */
data class Tips(val message: String, val operationTime: LocalDateTime = LocalDateTime.now())
