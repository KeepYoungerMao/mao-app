package com.mao.employee.repository

import com.mao.employee.entity.EmployeeDepartmentRefDo
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDepartmentRefRepository : CoroutineCrudRepository<EmployeeDepartmentRefDo, Int> {

    suspend fun findByEmployeeIdAndDepartmentId(employeeId: Int, departmentId: Int): EmployeeDepartmentRefDo?

    suspend fun countByEmployeeIdAndEnabled(employeeId: Int, enabled: Boolean): Long

    @Modifying
    @Query("update sys_employee_department_ref set primary_assignment = false where employee_id = :employeeId")
    suspend fun clearPrimaryAssignment(@Param("employeeId") employeeId: Int): Int

    @Modifying
    suspend fun deleteByEmployeeId(employeeId: Int)

}