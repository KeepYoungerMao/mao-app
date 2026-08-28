package com.mao.common.handler

import com.mao.common.config.DATETIME_FORMATTER
import java.time.Instant

/**
 * 将 Long? 毫秒时间戳安全地转换为 yyyy-MM-dd HH:mm:ss 格式的 String?
 */
val Long?.asDateStr: String?
    get() {
        if (this == null || this <= 0) return null
        return DATETIME_FORMATTER.format(Instant.ofEpochMilli(this))
    }