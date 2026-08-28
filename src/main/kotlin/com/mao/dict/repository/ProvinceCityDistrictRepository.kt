package com.mao.dict.repository

import com.mao.dict.entity.ProvinceCityDistrictDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProvinceCityDistrictRepository : CoroutineCrudRepository<ProvinceCityDistrictDo, Int>