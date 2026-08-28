package com.mao.extension.validate

import com.mao.service.DictService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 省市区校验器
 */
class ProvinceCityDistrictValidator(
    private val dictService: DictService,
) : ConstraintValidator<ProvinceCityDistrict, Int> {
    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull负责，此处放行
        if (value == null) return true
        return dictService.isProvinceCityDistrict(value)
    }
}