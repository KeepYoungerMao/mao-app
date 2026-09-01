package com.mao.common.validate

import com.mao.dict.cache.DictCache
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 字典校验器
 */
class DictValidator(
    private val dictCache: DictCache
) : ConstraintValidator<DictField, Int> {

    private lateinit var dictType: String

    override fun initialize(annotation: DictField) {
        dictType = annotation.name
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull/@NotBlank负责，此处放行
        if (value == null) return true
        return dictCache.isActiveDictItem(value, dictType)
    }
}