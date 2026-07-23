package com.mao.extension

import com.mao.entity.PageResponse
import com.mao.entity.query.PageQo
import com.mao.repository.PageableRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.repository.query.RelationalEntityInformation

/**
 * 通用分页查询接口默认实现类
 *
 * @param T 查询结果实体类
 * @param ID 查询结果实体类主键ID类型
 * @param Q 查询参数实体类，需继承 PageQo
 */
class BaseRepositoryImpl<T : Any, ID : Any, Q : PageQo>(
    entityInformation: RelationalEntityInformation<T, ID>,
    private val entityOperations: R2dbcEntityOperations,
    converter: R2dbcConverter
) : SimpleR2dbcRepository<T, ID>(entityInformation, entityOperations, converter), PageableRepository<T, Q> {

    private val entityClass: Class<T> = entityInformation.javaType

    override suspend fun page(request: Q): PageResponse<T> = page(request, Sort.unsorted())

    override suspend fun page(request: Q, sort: Sort): PageResponse<T> = coroutineScope {
        // 利用反射工具类自动构建 Criteria
        val criteria: Criteria = QueryBuilder.build(request)
        val query = Query.query(criteria)

        // 构建分页参数
        val pageRequest = if (sort.isUnsorted) {
            PageRequest.of(request.offset(), request.pageSize)
        } else {
            PageRequest.of(request.offset(), request.pageSize, sort)
        }

        val dataDeferred = async {
            entityOperations.select(entityClass)
                .matching(query.with(pageRequest))
                .all()
                .collectList()
                .awaitSingle()
        }

        val countDeferred = async {
            val currentHitCount = request.lastHitCount
            if (request.recount || currentHitCount == null || currentHitCount < 0) {
                entityOperations.count(query, entityClass).awaitSingleOrNull() ?: 0L
            } else {
                currentHitCount
            }
        }

        PageResponse(
            pageNum = request.pageNum,
            pageSize = request.pageSize,
            total = countDeferred.await(),
            records = dataDeferred.await()
        )
    }
}