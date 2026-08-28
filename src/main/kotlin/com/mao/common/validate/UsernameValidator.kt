package com.mao.common.validate

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 用户名校验器
 */
class UsernameValidator : ConstraintValidator<Username, String> {
    companion object {
        private val usernamePattern = Regex("^[a-zA-Z][a-zA-Z0-9_-]{2,19}$")
    }
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true
        return usernamePattern.matches(value)
    }
}