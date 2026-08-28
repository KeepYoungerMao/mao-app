package com.mao.user.repository

import com.mao.user.entity.UserProfileMaterialDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserProfileMaterialRepository : CoroutineCrudRepository<UserProfileMaterialDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_material
        WHERE user_id = :userId
        ORDER BY create_time ASC, id ASC
    """)
    suspend fun findAllByUserIdOrderByCreateTime(@Param("userId") userId: Int): Flow<UserProfileMaterialDo>

    @Modifying
    suspend fun deleteByUserId(userId: Int): Int

}