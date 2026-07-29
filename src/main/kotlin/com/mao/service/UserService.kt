package com.mao.service

import com.mao.entity.PageResponse
import com.mao.entity.domain.UserDo
import com.mao.entity.query.UserAddQo
import com.mao.entity.query.UserQo
import com.mao.entity.query.UserUpdateQo
import com.mao.entity.view.UserVo
import com.mao.extension.UserCreateEvent
import com.mao.mapper.UserMapper
import com.mao.repository.UserRepository
import com.mao.util.RandomUtils
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher
) {

    suspend fun searchUsers(request: UserQo): PageResponse<UserVo> {
        return userRepository.page(request).map { userMapper.toVo(it) }
    }

    suspend fun searchUser(id: Int): UserVo {
        return userRepository.findByIdOrThrow(id).let { userMapper.toVo(it) }
    }

    suspend fun saveUser(userAdd: UserAddQo): UserVo {
        // 生成密码
        val password = RandomUtils.pass(16)
        // 设置enable=false，要求第一次登录需更改密码
        val userDo: UserDo = userMapper.toDo(userAdd).apply {
            this.password = passwordEncoder.encode(password)
            this.enabled = true
            this.locked = false
            this.expired = false
            this.mustChangePassword = true
        }
        // 保存用户数据
        val savedUser: UserDo = userRepository.save(userDo)
        // 发布邮箱通知事件
        eventPublisher.publishEvent(UserCreateEvent(savedUser.email!!, password))
        return userMapper.toVo(savedUser)
    }

    suspend fun updateUser(userUpdate: UserUpdateQo): UserVo {
        val userDo = userRepository.findByIdOrThrow(userUpdate.id)
        val user = userRepository.save(userMapper.copyToExistDo(userUpdate, userDo))
        return userMapper.toVo(user)
    }

}