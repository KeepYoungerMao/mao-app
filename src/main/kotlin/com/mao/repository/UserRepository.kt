package com.mao.repository

import com.mao.entity.domain.UserDo
import com.mao.entity.query.UserQo
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<UserDo, Int, UserQo> {

    suspend fun findByUsername(username: String): UserDo?

}