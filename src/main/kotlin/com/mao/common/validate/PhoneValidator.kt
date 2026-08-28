package com.mao.common.validate

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 手机号校验器
 */
class PhoneValidator : ConstraintValidator<Phone, String> {
    companion object {
        private val phonePattern = Regex("^1[3-9][0-9]{9}$")
    }
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull/@NotBlank负责，此处放行
        if (value.isNullOrBlank()) return true
        return phonePattern.matches(value)
    }
}