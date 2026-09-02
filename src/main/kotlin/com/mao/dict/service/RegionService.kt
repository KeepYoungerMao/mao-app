package com.mao.dict.service

import com.mao.dict.cache.DictCache
import com.mao.dict.entity.RegionVo
import org.springframework.stereotype.Service

@Service
class RegionService(
    private val dictCache: DictCache
) {

    suspend fun searchRegionTree(): List<RegionVo> = dictCache.getProvinceCityDistrictTree()

}