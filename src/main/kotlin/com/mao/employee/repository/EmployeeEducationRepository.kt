package com.mao.employee.repository

import com.mao.employee.entity.EmployeeEducationDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeEducationRepository : CoroutineCrudRepository<EmployeeEducationDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_education
        WHERE employee_id = :employeeId
        ORDER BY start_date ASC, id ASC
    """)
    suspend fun findAllByEmployeeIdOrderByStartDate(@Param("employeeId") employeeId: Int): Flow<EmployeeEducationDo>

    @Modifying
    suspend fun deleteByEmployeeId(employeeId: Int): Int

}