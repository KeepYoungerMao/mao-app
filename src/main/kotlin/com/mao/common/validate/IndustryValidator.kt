package com.mao.common.validate

import com.mao.dict.cache.DictCache
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 行业校验器
 */
class IndustryValidator(
    private val dictCache: DictCache
) : ConstraintValidator<Industry, Int> {
    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull负责，此处放行
        if (value == null) return true
        return dictCache.isIndustry(value)
    }
}