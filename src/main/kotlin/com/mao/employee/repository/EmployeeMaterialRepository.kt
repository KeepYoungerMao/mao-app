package com.mao.employee.repository

import com.mao.employee.entity.EmployeeMaterialDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeMaterialRepository : CoroutineCrudRepository<EmployeeMaterialDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_material
        WHERE user_id = :userId
        ORDER BY create_time ASC, id ASC
    """)
    suspend fun findAllByUserIdOrderByCreateTime(@Param("userId") userId: Int): Flow<EmployeeMaterialDo>

    @Modifying
    suspend fun deleteByEmployeeId(employeeId: Int): Int

}