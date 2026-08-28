package com.mao.user.repository

import com.mao.role.entity.RoleDo
import com.mao.user.entity.UserRoleRefDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRefRepository: CoroutineCrudRepository<UserRoleRefDo, Int> {

    /**
     * 根据用户id查询关联的角色列表
     */
    @Query("""
    select r.*
    from sys_user_role_ref as ur
    inner join sys_role as r on ur.role_id = r.id
    where ur.user_id = :userId
    """)
    fun getRoleByUserId(@Param("userId") userId: Int): Flow<RoleDo>

    /**
     * 根据角色id查询与该角色相关联的用户名
     */
    @Query("""
    select b.username 
    from sys_user_role_ref as a
    inner join sys_user as b on a.user_id = b.id 
    where a.role_id = :roleId
    """)
    suspend fun getUsernameByRoleId(@Param("roleId") roleId: Int): Flow<String>

    /**
     * 根据用户名查询相关联的角色列表
     */
    @Query("""
    select c.*
    from sys_user as a
    left join sys_user_role_ref as b on a.id = b.user_id 
    left join sys_role as c on b.role_id = c.id 
    where a.username = :username
    """)
    suspend fun getRoleByUsername(@Param("username") username: String): Flow<RoleDo?>

    /**
     * 根据用户id删除关联数据
     */
    @Modifying
    suspend fun deleteByUserId(@Param("userId") userId: Int): Int

}