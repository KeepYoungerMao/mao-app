package com.mao.repository

import com.mao.entity.ErrorCode
import com.mao.entity.query.PageQo
import com.mao.ex.AppException
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