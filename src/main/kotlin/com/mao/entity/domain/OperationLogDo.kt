package com.mao.entity.domain

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_operation_log")
data class OperationLogDo(
    var id: Long? = null,
    var username: String? = null,
    var scope: String? = null,
    var module: String? = null,
    var operation: String? = null,
    var description: String? = null,
    var method: String? = null,
    var ip: String? = null,
    var success: Boolean? = null,
    var errorMessage: String? = null,
    var operationTime: LocalDateTime? = null,
    var cost: Long? = null
)
