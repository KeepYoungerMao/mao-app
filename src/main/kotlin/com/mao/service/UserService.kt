package com.mao.service

import com.mao.entity.*
import com.mao.ex.AppException
import com.mao.extension.PasswordHandler
import com.mao.listener.UserCreateEvent
import com.mao.listener.UserPasswordResetEvent
import com.mao.listener.UserStatusUpdateEvent
import com.mao.listener.UserStatusUpdateType
import com.mao.mapper.DepartmentMapper
import com.mao.mapper.RoleMapper
import com.mao.mapper.UserMapper
import com.mao.mapper.UserProfileMapper
import com.mao.repository.UserDepartmentRefRepository
import com.mao.repository.UserProfileRepository
import com.mao.repository.UserRepository
import com.mao.repository.UserRoleRefRepository
import com.mao.util.currentUser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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
    private val eventPublisher: ApplicationEventPublisher,
    private val userRoleRefRepository: UserRoleRefRepository,
    private val userDepartmentRefRepository: UserDepartmentRefRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userProfileMapper: UserProfileMapper,
    private val roleMapper: RoleMapper,
    private val departmentMapper: DepartmentMapper,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun searchUsers(request: UserQo): PageResponse<UserVo> = coroutineScope {
        val recordsDeferred = async {
            userRepository.searchUsers(
                username = request.username,
                phone = request.phone,
                email = request.email,
                expired = request.expired,
                locked = request.locked,
                enabled = request.enabled,
                roleId = request.roleId,
                departmentId = request.departmentId,
                pageSize = request.pageSize,
                offset = request.offset(),
            ).toList()
        }
        val countDeferred = async {
            val currentHitCount = request.lastHitCount
            if (request.recount || currentHitCount == null || currentHitCount < 0) {
                userRepository.countUsers(
                    username = request.username,
                    phone = request.phone,
                    email = request.email,
                    expired = request.expired,
                    locked = request.locked,
                    enabled = request.enabled,
                    roleId = request.roleId,
                    departmentId = request.departmentId,
                )
            } else {
                currentHitCount
            }
        }
        PageResponse(
            pageNum = request.pageNum,
            pageSize = request.pageSize,
            total = countDeferred.await(),
            records = recordsDeferred.await(),
        ).map { userMapper.toVo(it) }
    }

    suspend fun searchUser(id: Int?): UserDetailVo {
        val user = userRepository.findByIdOrThrow(id)
        val userId = user.id!!
        return coroutineScope {
            val profileDeferred = async {
                userProfileRepository.findByUserId(userId)
                    ?.let(userProfileMapper::toVo)
                    ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
            }
            val rolesDeferred = async {
                userRoleRefRepository.getRoleByUserId(userId)
                    .map(roleMapper::toVo)
                    .toList()
            }
            val departmentsDeferred = async {
                userDepartmentRefRepository.getDepartmentByUserId(userId)
                    .map(departmentMapper::toVo)
                    .toList()
            }
            UserDetailVo(
                user = userMapper.toVo(user),
                profile = profileDeferred.await(),
                roles = rolesDeferred.await(),
                departments = departmentsDeferred.await(),
            )
        }
    }

    @Transactional
    suspend fun saveUser(userAdd: UserAddQo): UserVo {
        // 生成密码
        val password = passwordHandler.generatePassword()
        // 补齐用户默认参数
        val userDo: UserDo = userMapper.toDo(userAdd).apply {
            this.password = passwordEncoder.encode(password)
            this.enabled = true
            this.locked = false
            this.expired = false
            this.passwordStatus = PasswordStatus.PASSWORD_UNCHANGE.code
        }
        // 保存用户数据
        val savedUser: UserDo = userRepository.save(userDo)
        // 补齐用户资料参数
        val userProfileDo: UserProfileDo = userProfileMapper.toDo(userAdd).apply {
            this.userId = savedUser.id
            this.userCode = "MS${savedUser.id}"
        }
        // 保存用户资料数据
        val savedUserProfile = userProfileRepository.save(userProfileDo)
        // 发布邮箱通知事件
        eventPublisher.publishEvent(UserCreateEvent(savedUserProfile.realName!!, savedUser.email!!, password))
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
        // 更新密码状态为2：密码已更新
        user.passwordStatus = PasswordStatus.PASSWORD_EDIT.code
        userRepository.save(user)
        // 发布密码变更事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(username, UserStatusUpdateType.PASSWORD_EDIT))
        return Tips("更新密码成功，请重新登陆")
    }

    suspend fun resetUserPassword(userPasswordReset: UserPasswordResetQo): Tips {
        val username = userPasswordReset.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val newPassword = passwordHandler.generatePassword()
        user.password = passwordEncoder.encode(newPassword)
        // 更新密码状态为3：密码已重置
        user.passwordStatus = PasswordStatus.PASSWORD_RESET.code
        userRepository.save(user)
        // 发布密码重置事件，用于邮箱通知
        eventPublisher.publishEvent(UserPasswordResetEvent(user.email!!, newPassword))
        // 发布密码重置事件，用于认证
        eventPublisher.publishEvent(UserStatusUpdateEvent(username, UserStatusUpdateType.PASSWORD_RESET))
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
        // 发布用户锁定事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.LOCKED))
        return userMapper.toVo(user)
    }

    suspend fun unlockUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.locked = false
        userRepository.save(user)
        return userMapper.toVo(user)
    }

    suspend fun enableUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.enabled = true
        userRepository.save(user)
        return userMapper.toVo(user)
    }

    suspend fun disableUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.enabled = false
        userRepository.save(user)
        // 发布用户禁用事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.DISABLED))
        return userMapper.toVo(user)
    }

    @Transactional
    suspend fun updateUserRole(userRoleUpdate: UserRoleUpdateQo): Tips {
        val id = userRoleUpdate.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        val roleIds = userRoleUpdate.roleIds ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 查询用户
        val user = userRepository.findById(id) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val operateUser = currentUser()
        log.info("user: [{}] operate: update user[{}] role ref.", operateUser, user.username)
        // 删除旧关联
        val count = userRoleRefRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete old user [{}] role ref [count: {}]", operateUser, user.username, count)
        // 新增新数据
        val userRoleRefs = roleIds.map { UserRoleRefDo(id = null, userId = user.id!!, roleId = it) }
        val refs = userRoleRefRepository.saveAll(userRoleRefs)
        log.info("user: [{}] operate: add new user [{}] role ref [count: {}]", operateUser, user.username, refs.count())
        // 发布角色改变事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.ROLES_EDIT))
        return Tips("角色更新成功")
    }

    suspend fun updateUserProfile(userProfileUpdate: UserProfileUpdateQo): UserProfileVo {
        val id = userProfileUpdate.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        val userProfile = userProfileRepository.findById(id) ?: throw AppException(ErrorCode.BAD_REQUEST)
        val userProfileUpdateDo = userProfileMapper.copyToExistDo(userProfileUpdate, userProfile)
        val updatedUserProfile = userProfileRepository.save(userProfileUpdateDo)
        return userProfileMapper.toVo(updatedUserProfile)
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
        val count1 = userRoleRefRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] role ref [count: {}]", operateUser, user.username, count1)
        // TODO delete user_profile_*
        // 发布角色删除事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.DELETED))
        return Tips("数据删除成功")
    }

    /**
     * 重置密码状态为正常状态
     * 该方法用于，密码状态特殊时（1：首次创建用户时密码为更改，2：密码已变更，3：密码已重置），不允许使用刷新token的方式登录，
     * 等用户使用创建token方式登录成功后，调用此方法，将特殊密码状态更新为正常状态 0
     */
    suspend fun resetUserPasswordStatus(userId: Int) {
        val user = userRepository.findByIdOrThrow(userId)
        user.passwordStatus = PasswordStatus.OK.code
        userRepository.save(user)
    }

}