package com.mao.entity.view

data class OperationLogVo(
    val id: Long? = null,
    val username: String? = null,
    val scope: String? = null,
    val module: String? = null,
    val operation: String? = null,
    val description: String? = null,
    val method: String? = null,
    val ip: String? = null,
    val success: Boolean? = null,
    val errorMessage: String? = null,
    val operationTime: Long? = null,
    val cost: Long? = null
)
