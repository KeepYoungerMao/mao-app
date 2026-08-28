package com.mao.extension.validate

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IndustryValidator::class])
annotation class Industry(
    val message: String = "行业ID不存在",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
