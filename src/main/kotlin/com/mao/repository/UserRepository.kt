package com.mao.repository

import com.mao.entity.UserDo
import com.mao.entity.UserQo
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<UserDo, Int, UserQo> {

    suspend fun findByUsername(username: String): UserDo?

}