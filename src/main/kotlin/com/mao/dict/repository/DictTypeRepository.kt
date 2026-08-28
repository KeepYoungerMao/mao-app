package com.mao.dict.repository

import com.mao.dict.entity.DictTypeDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DictTypeRepository : CoroutineCrudRepository<DictTypeDo, Int> {
    suspend fun findByName(name: String): DictTypeDo?
}