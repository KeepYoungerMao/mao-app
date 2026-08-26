package com.mao.repository

import com.mao.entity.UserProfileEducationDo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserProfileEducationRepository : CoroutineCrudRepository<UserProfileEducationDo, Int> {

    @Query("""
        SELECT *
        FROM sys_user_profile_education
        WHERE user_id = :userId
        ORDER BY start_date ASC, id ASC
    """)
    suspend fun findAllByUserIdOrderByStartDate(@Param("userId") userId: Int): Flow<UserProfileEducationDo>

    @Modifying
    suspend fun deleteByUserId(userId: Int): Int

}