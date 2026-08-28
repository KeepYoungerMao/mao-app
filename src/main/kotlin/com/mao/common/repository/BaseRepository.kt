package com.mao.common.repository

import com.mao.common.ex.AppException
import com.mao.common.entity.ErrorCode
import com.mao.common.entity.PageQo
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@NoRepositoryBean
interface BaseRepository<T : Any, ID : Any, Q : PageQo> : CoroutineCrudRepository<T, ID>, PageableRepository<T, Q> {

    suspend fun findByIdOrThrow(id: ID?): T {
        if (id == null) {
            throw AppException(ErrorCode.BAD_REQUEST)
        }
        return findById(id) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
    }

}