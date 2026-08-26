package com.mao.repository

import com.mao.entity.UserProfileRelationshipDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRelationshipRepository : CoroutineCrudRepository<UserProfileRelationshipDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_relationship
        WHERE user_id = :userId
        ORDER BY create_time ASC, id ASC
    """)
    suspend fun findAllByUserIdOrderByCreateTime(@Param("userId") userId: Int): Flow<UserProfileRelationshipDo>

    @Modifying
    suspend fun deleteByUserId(userId: Int): Int

}