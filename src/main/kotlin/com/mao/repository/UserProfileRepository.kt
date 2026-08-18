package com.mao.repository

import com.mao.entity.UserProfileDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository: CoroutineCrudRepository<UserProfileDo, Int> {

	suspend fun findByUserId(userId: Int): UserProfileDo?

}