package com.mao.user.repository

import com.mao.department.entity.DepartmentDo
import com.mao.user.entity.UserDepartmentRefDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserDepartmentRefRepository : CoroutineCrudRepository<UserDepartmentRefDo, Int> {

    suspend fun findByUserIdAndDepartmentId(userId: Int, departmentId: Int): UserDepartmentRefDo?

    suspend fun countByUserIdAndEnabled(userId: Int, enabled: Boolean): Long

    @Modifying
    @Query("update sys_user_department_ref set primary_assignment = false where user_id = :userId")
    suspend fun clearPrimaryAssignment(@Param("userId") userId: Int): Int

    /**
     * 根据用户id查询直接关联的部门列表
     */
    @Query("""
    select d.*
    from sys_user_department_ref as ud
    inner join sys_department as d on ud.department_id = d.id
    where ud.user_id = :userId
    order by d.sort_order asc, d.id asc
    """)
    fun getDepartmentByUserId(@Param("userId") userId: Int): Flow<DepartmentDo>
}