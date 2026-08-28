package com.mao.common.validate

import com.mao.dict.entity.DictType
import com.mao.dict.service.DictService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 字典校验器
 */
class DictValidator(
    private val dictService: DictService,
) : ConstraintValidator<Dict, Int> {
    private lateinit var type: DictType

    override fun initialize(annotation: Dict) {
        type = annotation.type
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull/@NotBlank负责，此处放行
        if (value == null) return true
        return dictService.isActiveItem(type, value)
    }
}