package com.mao.common.validate

import com.mao.dict.entity.RegionType
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProvinceCityDistrictValidator::class])
annotation class ProvinceCityDistrict(
    val type: RegionType,
    val message: String = "省市区ID不存在",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
