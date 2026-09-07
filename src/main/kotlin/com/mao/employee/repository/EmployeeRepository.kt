package com.mao.employee.repository

import com.mao.common.repository.BaseRepository
import com.mao.employee.entity.EmployeeDo
import com.mao.employee.entity.EmployeeQo
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface EmployeeRepository: BaseRepository<EmployeeDo, Int, EmployeeQo> {

	suspend fun countByUserId(@Param("userId") userId: Int): Int

	@Modifying
	@Query("""
		UPDATE sys_employee SET user_id = null WHERE `id` = :id
	""")
	suspend fun unbindUserById(@Param("id") id: Int): Int

}