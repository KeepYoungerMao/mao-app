package com.mao.dict.repository

import com.mao.dict.entity.RegionDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegionRepository : CoroutineCrudRepository<RegionDo, Int>