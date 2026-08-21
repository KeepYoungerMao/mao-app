package com.mao.repository

import com.mao.entity.DictItemDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DictItemRepository : CoroutineCrudRepository<DictItemDo, Int>