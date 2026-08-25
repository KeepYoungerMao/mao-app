package com.mao.repository

import com.mao.entity.DictItemDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DictItemRepository : CoroutineCrudRepository<DictItemDo, Int> {
    
    suspend fun findByPidAndName(pid: Int, name: String): DictItemDo?
        
    suspend fun findByPidAndNameAndIdNot(pid: Int, name: String, id: Int): DictItemDo?
    
}