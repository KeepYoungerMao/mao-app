package com.mao.repository

import com.mao.entity.ProvinceCityDistrictDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProvinceCityDistrictRepository : CoroutineCrudRepository<ProvinceCityDistrictDo, Int>