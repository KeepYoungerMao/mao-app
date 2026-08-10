package com.mao.repository

import com.mao.entity.RoleDo
import com.mao.entity.UserRoleRefDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRefRepository: CoroutineCrudRepository<UserRoleRefDo, Int> {

    @Query("""
    select b.username 
    from sys_user_role_ref as a
    inner join sys_user as b on a.user_id = b.id 
    where a.role_id = :roleId
    """)
    suspend fun getUsernameByRoleId(@Param("roleId") roleId: Int): Flow<String>

    @Query("""
    select c.*
    from sys_user as a
    left join sys_user_role_ref as b on a.id = b.user_id 
    left join sys_role as c on b.role_id = c.id 
    where a.username = :username
    """)
    suspend fun getRoleByUsername(@Param("username") username: String): Flow<RoleDo?>

}