package com.mao.dict.service

import com.mao.dict.cache.DictCache
import com.mao.dict.entity.IndustryVo
import org.springframework.stereotype.Service

@Service
class IndustryService(
    private val dictCache: DictCache
) {

    suspend fun searchIndustryTree(): List<IndustryVo> = dictCache.getIndustryTree()

}