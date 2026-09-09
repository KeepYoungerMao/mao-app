package com.mao.employee.repository

import com.mao.employee.entity.EmployeeCodeSequenceDo
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeCodeSequenceRepository : CoroutineCrudRepository<EmployeeCodeSequenceDo, Int> {

    suspend fun findBySequenceYearAndDepartmentCode(year: Int, departmentCode: String): EmployeeCodeSequenceDo?

    @Modifying
    @Query("""
        update sys_employee_code_sequence
        set current_value = :nextValue, update_time = CURRENT_TIMESTAMP
        where id = :id and current_value = :currentValue
    """)
    suspend fun updateCurrentValue(
        @Param("id") id: Int,
        @Param("currentValue") currentValue: Int,
        @Param("nextValue") nextValue: Int
    ): Int

}
