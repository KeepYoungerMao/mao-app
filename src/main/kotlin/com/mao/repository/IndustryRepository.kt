package com.mao.repository

import com.mao.entity.IndustryDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IndustryRepository : CoroutineCrudRepository<IndustryDo, Int> {
    fun findAllByOrderByPidAscIdAsc(): Flow<IndustryDo>
}