package com.mao.department.repository

import com.mao.department.entity.DepartmentDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : CoroutineCrudRepository<DepartmentDo, Int> {

    suspend fun countByParentId(parentId: Int): Long

    fun findByParentId(parentId: Int): Flow<DepartmentDo>

    suspend fun findByDepartmentCode(departmentCode: String): DepartmentDo?
}
