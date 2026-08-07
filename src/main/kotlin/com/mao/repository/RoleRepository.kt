package com.mao.repository

import com.mao.entity.RoleDo
import com.mao.entity.RoleQo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query

interface RoleRepository: BaseRepository<RoleDo, Int, RoleQo> {

    @Query("""
    SELECT b.* FROM sys_user_role_ref as a INNER JOIN sys_role as b ON a.role_id = b.id
    WHERE a.user_id = :userId
    """)
    suspend fun findRolesByUserId(userId: Int): Flow<RoleDo>

}