package com.mao.repository

import com.mao.entity.PageResponse
import com.mao.entity.PageQo
import org.springframework.data.domain.Sort

interface PageableRepository<T: Any, Q: PageQo> {

    suspend fun page(request: Q): PageResponse<T>

    suspend fun page(request: Q, sort: Sort): PageResponse<T>

}