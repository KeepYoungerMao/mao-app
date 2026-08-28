package com.mao.extension.validate

import com.mao.service.DictService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 行业校验器
 */
class IndustryValidator(
    private val dictService: DictService,
) : ConstraintValidator<Industry, Int> {
    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull负责，此处放行
        if (value == null) return true
        return dictService.isIndustry(value)
    }
}