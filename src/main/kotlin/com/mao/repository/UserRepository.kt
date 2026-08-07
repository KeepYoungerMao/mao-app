package com.mao.repository

import com.mao.entity.UserDo
import com.mao.entity.UserQo
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<UserDo, Int, UserQo> {

    suspend fun findByUsername(username: String): UserDo?

    @Modifying
    @Query("DELETE FROM sys_user_role_ref WHERE user_id = :userId")
    suspend fun deleteUserRoleRefByUserId(@Param("userId") userId: Int): Int

}