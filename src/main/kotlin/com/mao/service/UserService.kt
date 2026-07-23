package com.mao.service

import com.mao.entity.PageResponse
import com.mao.entity.query.UserAddQo
import com.mao.entity.query.UserQo
import com.mao.entity.view.UserVo
import com.mao.mapper.UserMapper
import com.mao.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val userMapper: UserMapper) {

    suspend fun searchUsers(request: UserQo): PageResponse<UserVo> {
        return userRepository.page(request).map { userMapper.toVo(it) }
    }

    suspend fun searchUser(id: Int): UserVo {
        return userRepository.findByIdOrThrow(id).let { userMapper.toVo(it) }
    }

    suspend fun saveUser(userAdd: UserAddQo): UserVo {
        return userRepository.save(userMapper.toNewDo(userAdd)).let { userMapper.toVo(it) }
    }

    suspend fun updateUser(userAdd: UserAddQo): UserVo {
        return userRepository.findByIdOrThrow(userAdd.id)
            .let { user -> userMapper.copyToExistDo(userAdd, user) }
            .let { userRepository.save(it) }
            .let { userMapper.toVo(it) }
    }

}