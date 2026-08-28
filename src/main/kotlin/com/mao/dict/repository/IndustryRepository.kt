package com.mao.dict.repository

import com.mao.dict.entity.IndustryDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IndustryRepository : CoroutineCrudRepository<IndustryDo, Int> {
    fun findAllByOrderByPidAscIdAsc(): Flow<IndustryDo>
}