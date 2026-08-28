package com.mao.role.repository

import com.mao.role.entity.RolePermissionRefDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RolePermissionRefRepository: CoroutineCrudRepository<RolePermissionRefDo, Int> {

    @Query("""
    select DISTINCT b.api 
    from sys_role_permission_ref as a
    left join sys_permission as b on a.permission_id = b.id 
    where a.role_id in(:roleIds)
    """)
    suspend fun getPermissionByRoleIdIn(@Param("roleIds") roleIds: List<Int>): Flow<String?>

}


