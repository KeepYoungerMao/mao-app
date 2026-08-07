package com.mao.service

import com.mao.entity.*
import com.mao.ex.AppException
import com.mao.extension.PasswordHandler
import com.mao.listener.UserCreateEvent
import com.mao.listener.UserPasswordResetEvent
import com.mao.mapper.UserMapper
import com.mao.repository.UserRepository
import com.mao.util.currentUser
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val passwordHandler: PasswordHandler,
    private val eventPublisher: ApplicationEventPublisher
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun searchUsers(request: UserQo): PageResponse<UserVo> {
        return userRepository.page(request).map { userMapper.toVo(it) }
    }

    suspend fun searchUser(id: Int?): UserVo {
        return userRepository.findByIdOrThrow(id).let { userMapper.toVo(it) }
    }

    suspend fun saveUser(userAdd: UserAddQo): UserVo {
        // 生成密码
        val password = passwordHandler.generatePassword()
        // 补齐默认参数
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

    suspend fun updateUserPassword(userPasswordUpdate: UserPasswordUpdateQo): Tips {
        val username = userPasswordUpdate.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        val oldPassword = passwordHandler.decryptPassword(userPasswordUpdate.oldPassword, userPasswordUpdate.timestamp)
        val newPassword = passwordHandler.decryptPassword(userPasswordUpdate.newPassword, userPasswordUpdate.timestamp)
        if (!passwordHandler.isLegalPassword(newPassword)) {
            throw AppException(ErrorCode.ILLEGAL_PASSWORD)
        }
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw AppException(ErrorCode.PASSWORD_ERROR)
        }
        user.password = passwordEncoder.encode(newPassword)
        // 更新是否需要更改密码为false
        user.mustChangePassword = false
        userRepository.save(user)
        return Tips("更新密码成功，请重新登陆")
    }

    suspend fun resetUserPassword(userPasswordReset: UserPasswordResetQo): Tips {
        val username = userPasswordReset.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val newPassword = passwordHandler.generatePassword()
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
        eventPublisher.publishEvent(UserPasswordResetEvent(user.email!!, newPassword))
        return Tips("密码重置成功")
    }

    suspend fun renewUser(userRenewal: UserRenewalQo): UserVo {
        val username = userRenewal.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        val expireTime = userRenewal.expireTime ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (LocalDateTime.now() >= expireTime) {
            throw AppException(ErrorCode.ILLEGAL_EXPIRE_TIME)
        }
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        user.expireTime = expireTime
        user.expired = false
        userRepository.save(user)
        return userMapper.toVo(user)
    }

    suspend fun lockUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.locked = true
        userRepository.save(user)
        return userMapper.toVo(user)
    }

    suspend fun unlockUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.locked = false
        userRepository.save(user)
        return userMapper.toVo(user)
    }

    /**
     * 删除用户数据
     * 谨慎操作：删除用户会同时删除：用户资料、用户教育经历、工作经历、人群关系、递交材料等信息
     * 一般禁用用户操作即可，非特殊情况不删除用户数据
     */
    @Transactional
    suspend fun deleteUser(id: Int?): Tips {
        val operateUser = currentUser()
        val user = userRepository.findByIdOrThrow(id)
        log.info("user: [{}] operate: delete user [{}]", operateUser, user.username)
        userRepository.deleteById(user.id!!)
        // delete user_role_ref
        val count1 = userRepository.deleteUserRoleRefByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] role ref [count: {}]", operateUser, user.username, count1)
        // TODO delete user_profile_*
        return Tips("数据删除成功")
    }

}