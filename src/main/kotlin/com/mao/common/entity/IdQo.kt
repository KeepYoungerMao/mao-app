package com.mao.common.entity

import jakarta.validation.constraints.NotNull

/**
 * 根据数据ID进行操作的请求参数。
 */
data class IdQo<T>(
    @field:NotNull(message = "请传递数据ID以操作")
    val id: T?
)
