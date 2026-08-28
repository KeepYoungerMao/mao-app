package com.mao.common.repository

import com.mao.common.entity.PageQo
import com.mao.common.entity.PageResponse
import org.springframework.data.domain.Sort

interface PageableRepository<T: Any, Q: PageQo> {

    suspend fun page(request: Q): PageResponse<T>

    suspend fun page(request: Q, sort: Sort): PageResponse<T>

}