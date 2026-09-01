package com.mao.common.validate

import com.mao.dict.cache.DictCache
import com.mao.dict.entity.RegionType
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 省市区校验器
 */
class ProvinceCityDistrictValidator(
    private val dictCache: DictCache,
) : ConstraintValidator<ProvinceCityDistrict, Int> {

    private lateinit var regionType: RegionType

    override fun initialize(annotation: ProvinceCityDistrict) {
        regionType = annotation.type
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        // null值由@NotNull负责，此处放行
        if (value == null) return true
        return dictCache.isProvinceCityDistrict(value, regionType)
    }

}