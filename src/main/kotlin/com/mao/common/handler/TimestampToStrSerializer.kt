package com.mao.common.handler

import com.mao.common.config.DATETIME_FORMATTER
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import java.time.Instant

/**
 * Jackson序列化时，将Long值转化为日期时间字符串格式的字符串
 * 使用方式：
 * 在需要序列化的类的字段上添加注解：@JsonSerialize(using = TimestampToStrSerializer::class)
 */
class TimestampToStrSerializer: ValueSerializer<Long>() {

    override fun serialize(value: Long?, gen: JsonGenerator?, ctxt: SerializationContext?) {
        if (value == null || value <= 0) {
            gen?.writeNull()
        } else {
            gen?.writeString(DATETIME_FORMATTER.format(Instant.ofEpochMilli(value)))
        }
    }

}