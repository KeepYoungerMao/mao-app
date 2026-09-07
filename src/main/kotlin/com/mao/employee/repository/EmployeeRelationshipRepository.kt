package com.mao.employee.repository

import com.mao.employee.entity.EmployeeRelationshipDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRelationshipRepository : CoroutineCrudRepository<EmployeeRelationshipDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_relationship
        WHERE employee_id = :employeeId
        ORDER BY create_time ASC, id ASC
    """)
    suspend fun findAllByEmployeeIdOrderByCreateTime(@Param("employeeId") employeeId: Int): Flow<EmployeeRelationshipDo>

    @Modifying
    suspend fun deleteByEmployeeId(employeeId: Int): Int

}