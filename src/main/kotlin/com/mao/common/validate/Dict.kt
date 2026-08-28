package com.mao.common.validate

import com.mao.dict.entity.DictType
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DictValidator::class])
annotation class Dict(
    val type: DictType,
    val message: String = "字典项不存在或已停用",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
