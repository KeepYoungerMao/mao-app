package com.mao.common.validate

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DictValidator::class])
annotation class DictField(
    val name: String,
    val message: String = "字典项不存在或已停用",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
