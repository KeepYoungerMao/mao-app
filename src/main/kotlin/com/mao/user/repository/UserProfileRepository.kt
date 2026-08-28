package com.mao.user.repository

import com.mao.user.entity.UserProfileDo
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserProfileRepository: CoroutineCrudRepository<UserProfileDo, Int> {

	suspend fun findByUserId(userId: Int): UserProfileDo?
		
	@Modifying
	suspend fun deleteByUserId(userId: Int): Int

}