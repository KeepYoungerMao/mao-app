package com.mao.extension.validate

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

/**
 * 身份证校验器
 */
class IdCardValidator : ConstraintValidator<IdCard, String> {
    companion object {
        // 11-15 北京、天津、河北、山西、内蒙古
        // 21-23 辽宁、吉林、黑龙江
        // 31-37 上海、江苏、浙江、安徽、福建、江西、山东
        // 41-46 河南、湖北、湖南、广东、广西、海南
        // 50-54 重庆、四川、贵州、云南、西藏
        // 61-65 陕西、甘肃、青海、宁夏、新疆
        // 71-82 台湾、香港、澳门
        private val PROVINCES_CODE = setOf(
            11, 12, 13, 14, 15,
            21, 22, 23,
            31, 32, 33, 34, 35, 36, 37,
            41, 42, 43, 44, 45, 46,
            50, 51, 52, 53, 54,
            61, 62, 63, 64, 65,
            71, 81, 82
        )
        // 18位身份证加权因子
        private val WEIGHTS = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
        // 18位身份证校验码映射
        private val CHECK_CODES = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
        // 日期严格模式校验，不允许非法日期
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT)
    }
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true
        val id = value.uppercase()
        return when(id.length) {
            18 -> validate18(id)
            15 -> validate15(id)
            else -> false
        }
    }

    private fun validate18(id: String): Boolean {
        // 17位数字 + 1位数字/X
        if (!id.substring(0, 17).all { it.isDigit() }) return false
        if (!(id[17].isDigit() || id[17] == 'X')) return false
        // 省份
        val provinceCode = id.substring(0, 2).toIntOrNull() ?: return false
        if (provinceCode !in PROVINCES_CODE) return false
        // 日期校验
        val dateStr = id.substring(6, 14)
        val birthDate = try {
            LocalDate.parse(dateStr, DATE_FORMAT)
        } catch (_: Exception) {
            return false
        }
        if (birthDate.isAfter(LocalDate.now())) return false
        // 校验码校验 (ISO 7064:1983 MOD 11-2)
        val sum = id.substring(0, 17).mapIndexed { index, char ->
            (char.digitToInt()) * WEIGHTS[index]
        }.sum()
        val expectedCheckCode = CHECK_CODES[sum % 11]
        return id[17] == expectedCheckCode
    }
    private fun validate15(id: String): Boolean {
        // 15位数字
        if (!id.all { it.isDigit() }) return false
        // 省份
        val provinceCode = id.substring(0, 2).toIntOrNull() ?: return false
        if (provinceCode !in PROVINCES_CODE) return false
        // 日期
        val dateStr = "19" + id.substring(6, 12)
        val birthDate = try {
            LocalDate.parse(dateStr, DATE_FORMAT)
        } catch (_: Exception) {
            return false
        }
        return !birthDate.isAfter(LocalDate.now())
    }
}