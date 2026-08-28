package com.mao.user.service

import com.mao.auth.handler.PasswordHandler
import com.mao.common.entity.ErrorCode
import com.mao.common.entity.PageResponse
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.common.listener.UserCreateEvent
import com.mao.common.listener.UserPasswordResetEvent
import com.mao.common.listener.UserStatusUpdateEvent
import com.mao.common.listener.UserStatusUpdateType
import com.mao.common.util.currentUser
import com.mao.department.mapper.DepartmentViewMapper
import com.mao.department.repository.DepartmentRepository
import com.mao.role.mapper.RoleViewMapper
import com.mao.user.entity.*
import com.mao.user.mapper.*
import com.mao.user.repository.*
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
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val passwordHandler: PasswordHandler,
    private val eventPublisher: ApplicationEventPublisher,
    private val userRoleRefRepository: UserRoleRefRepository,
    private val userDepartmentRefRepository: UserDepartmentRefRepository,
    private val departmentRepository: DepartmentRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userProfileEducationRepository: UserProfileEducationRepository,
    private val userProfileWorkRepository: UserProfileWorkRepository,
    private val userProfileRelationshipRepository: UserProfileRelationshipRepository,
    private val userProfileMaterialRepository: UserProfileMaterialRepository
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
        ).map { UserViewMapper.map(it) }
    }

    suspend fun searchUser(id: Int?): UserDetailVo {
        val user = userRepository.findByIdOrThrow(id)
        val userId = user.id!!
        return coroutineScope {
            val profileDeferred = async {
                userProfileRepository.findByUserId(userId)
                    ?.let(UserProfileViewMapper::map)
                    ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
            }
            val rolesDeferred = async {
                userRoleRefRepository.getRoleByUserId(userId)
                    .map(RoleViewMapper::map)
                    .toList()
            }
            val departmentsDeferred = async {
                userDepartmentRefRepository.getDepartmentByUserId(userId)
                    .map(DepartmentViewMapper::map)
                    .toList()
            }
            UserDetailVo(
                user = UserViewMapper.map(user),
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
        val userDo: UserDo = UserCreateMapper.map(userAdd).apply {
            this.password = passwordEncoder.encode(password)
            this.enabled = true
            this.locked = false
            this.expired = false
            this.passwordStatus = PasswordStatus.PASSWORD_UNCHANGE.code
        }
        // 保存用户数据
        val savedUser: UserDo = userRepository.save(userDo)
        // 补齐用户资料参数
        val userProfileDo: UserProfileDo = UserProfileCreateMapper.map(userAdd).apply {
            this.userId = savedUser.id
            this.userCode = "MS${savedUser.id}"
        }
        // 保存用户资料数据
        val savedUserProfile = userProfileRepository.save(userProfileDo)
        // 发布邮箱通知事件
        eventPublisher.publishEvent(UserCreateEvent(savedUserProfile.realName!!, savedUser.email!!, password))
        return UserViewMapper.map(savedUser)
    }

    suspend fun updateUser(userUpdate: UserUpdateQo): UserVo {
        val userDo = userRepository.findByIdOrThrow(userUpdate.id)
        val user = userRepository.save(UserMapper.copyToExistDo(userUpdate, userDo))
        return UserViewMapper.map(user)
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
        return UserViewMapper.map(user)
    }

    suspend fun lockUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.locked = true
        userRepository.save(user)
        // 发布用户锁定事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.LOCKED))
        return UserViewMapper.map(user)
    }

    suspend fun unlockUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.locked = false
        userRepository.save(user)
        return UserViewMapper.map(user)
    }

    suspend fun enableUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.enabled = true
        userRepository.save(user)
        return UserViewMapper.map(user)
    }

    suspend fun disableUser(id: Int?): UserVo {
        val user = userRepository.findByIdOrThrow(id)
        user.enabled = false
        userRepository.save(user)
        // 发布用户禁用事件
        eventPublisher.publishEvent(UserStatusUpdateEvent(user.username!!, UserStatusUpdateType.DISABLED))
        return UserViewMapper.map(user)
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
        val userProfileUpdateDo = UserProfileMapper.copyToExistDo(userProfileUpdate, userProfile)
        val updatedUserProfile = userProfileRepository.save(userProfileUpdateDo)
        return UserProfileViewMapper.map(updatedUserProfile)
    }

    suspend fun searchUserProfileEducations(userId: Int?): List<UserProfileEducationVo> {
        val id = userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(id)
        return userProfileEducationRepository.findAllByUserIdOrderByStartDate(id)
            .map(UserProfileEducationViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createUserProfileEducation(request: UserProfileEducationAddQo): UserProfileEducationVo {
        val userId = request.userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(userId)
        val education = userProfileEducationRepository.save(UserProfileEducationCreateMapper.map(request))
        return UserProfileEducationViewMapper.map(education)
    }

    @Transactional
    suspend fun updateUserProfileEducation(request: UserProfileEducationUpdateQo): UserProfileEducationVo {
        val education = userProfileEducationRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = userProfileEducationRepository.save(
            UserProfileEducationMapper.copyToExistDo(request, education)
        )
        return UserProfileEducationViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteUserProfileEducation(id: Int?): Tips {
        val educationId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        userProfileEducationRepository.findById(educationId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        userProfileEducationRepository.deleteById(educationId)
        return Tips("数据删除成功")
    }

    suspend fun searchUserProfileWorks(userId: Int?): List<UserProfileWorkVo> {
        val id = userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(id)
        return userProfileWorkRepository.findAllByUserIdOrderByStartDate(id)
            .map(UserProfileWorkViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createUserProfileWork(request: UserProfileWorkAddQo): UserProfileWorkVo {
        val userId = request.userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(userId)
        val work = userProfileWorkRepository.save(UserProfileWorkCreateMapper.map(request))
        return UserProfileWorkViewMapper.map(work)
    }

    @Transactional
    suspend fun updateUserProfileWork(request: UserProfileWorkUpdateQo): UserProfileWorkVo {
        val work = userProfileWorkRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = userProfileWorkRepository.save(UserProfileWorkMapper.copyToExistDo(request, work))
        return UserProfileWorkViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteUserProfileWork(id: Int?): Tips {
        val workId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        userProfileWorkRepository.findById(workId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        userProfileWorkRepository.deleteById(workId)
        return Tips("数据删除成功")
    }

    suspend fun searchUserProfileRelationships(userId: Int?): List<UserProfileRelationshipVo> {
        val id = userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(id)
        return userProfileRelationshipRepository.findAllByUserIdOrderByCreateTime(id)
            .map(UserProfileRelationshipViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createUserProfileRelationship(
        request: UserProfileRelationshipAddQo
    ): UserProfileRelationshipVo {
        val userId = request.userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(userId)
        val relationship = userProfileRelationshipRepository.save(UserProfileRelationshipCreateMapper.map(request))
        return UserProfileRelationshipViewMapper.map(relationship)
    }

    @Transactional
    suspend fun updateUserProfileRelationship(
        request: UserProfileRelationshipUpdateQo
    ): UserProfileRelationshipVo {
        val relationship = userProfileRelationshipRepository.findById(
            request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        ) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = userProfileRelationshipRepository.save(
            UserProfileRelationshipMapper.copyToExistDo(request, relationship)
        )
        return UserProfileRelationshipViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteUserProfileRelationship(id: Int?): Tips {
        val relationshipId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        userProfileRelationshipRepository.findById(relationshipId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        userProfileRelationshipRepository.deleteById(relationshipId)
        return Tips("数据删除成功")
    }

    suspend fun searchUserProfileMaterials(userId: Int?): List<UserProfileMaterialVo> {
        val id = userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(id)
        return userProfileMaterialRepository.findAllByUserIdOrderByCreateTime(id)
            .map(UserProfileMaterialViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createUserProfileMaterial(request: UserProfileMaterialAddQo): UserProfileMaterialVo {
        val userId = request.userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        userRepository.findByIdOrThrow(userId)
        val material = userProfileMaterialRepository.save(UserProfileMaterialCreateMapper.map(request))
        return UserProfileMaterialViewMapper.map(material)
    }

    @Transactional
    suspend fun updateUserProfileMaterial(request: UserProfileMaterialUpdateQo): UserProfileMaterialVo {
        val material = userProfileMaterialRepository.findById(
            request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        ) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = userProfileMaterialRepository.save(
            UserProfileMaterialMapper.copyToExistDo(request, material)
        )
        return UserProfileMaterialViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteUserProfileMaterial(id: Int?): Tips {
        val materialId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        userProfileMaterialRepository.findById(materialId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        userProfileMaterialRepository.deleteById(materialId)
        return Tips("数据删除成功")
    }

    /**
     * 新增用户部门关联
     */
    @Transactional
    suspend fun createUserDepartment(request: UserDepartmentAddQo): Tips {
        val userId = request.userId ?: throw AppException(ErrorCode.BAD_REQUEST)
        val departmentId = request.departmentId ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 用户排他锁，检验用户是否存在，同时避免并发操作导致的脏数据
        userRepository.findByIdForUpdate(userId) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        // 检验部门是否存在
        val department = departmentRepository.findById(departmentId)
            ?: throw AppException(ErrorCode.BAD_REQUEST, "部门不存在")
        // 检验部门是否允许分配成员
        if (department.memberAssignable != true) {
            throw AppException(ErrorCode.BAD_REQUEST, "该部门不允许分配成员")
        }
        // 检验日期是否合法
        validateDepartmentDate(request.startDate, request.endDate)
        // 检验用户是否已关联该部门
        if (userDepartmentRefRepository.findByUserIdAndDepartmentId(userId, departmentId) != null) {
            throw AppException(ErrorCode.BAD_REQUEST, "用户已关联该部门")
        }
        // 如果新增的是主职，先清除该用户已有部门的主职状态
        if (request.primaryAssignment == true) {
            userDepartmentRefRepository.clearPrimaryAssignment(userId)
        }
        // 保存数据
        userDepartmentRefRepository.save(
            UserDepartmentRefDo(
                id = null,
                userId = userId,
                departmentId = departmentId,
                positionId = request.positionId,
                primaryAssignment = request.primaryAssignment,
                startDate = request.startDate,
                endDate = request.endDate,
                enabled = true, // 新增时默认启用
            )
        )
        return Tips("用户部门关联创建成功")
    }

    /** 更新基础字段、主职状态或启用状态。 */
    @Transactional
    suspend fun updateUserDepartment(request: UserDepartmentUpdateQo): Tips {
        val id = request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 查询当前关联数据
        val current = userDepartmentRefRepository.findById(id) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        // 用户排他锁，检验用户是否存在，同时避免并发操作导致的脏数据
        userRepository.findByIdForUpdate(current.userId) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        // 检验日期是否合法
        if (request.startDate != null) {
            current.startDate = request.startDate
        }
        if (request.endDate != null) {
            current.endDate = request.endDate
        }
        validateDepartmentDate(current.startDate, current.endDate)
        // 检验职务ID
        if (request.positionId != null) {
            current.positionId = request.positionId
        }
        // 处理主职状态
        if (request.primaryAssignment != null) {
            current.primaryAssignment = request.primaryAssignment
            if (request.primaryAssignment) {
                userDepartmentRefRepository.clearPrimaryAssignment(current.userId)
            }
        }
        // 处理启用状态
        if (request.enabled != null) {
            current.enabled = request.enabled
            if (current.enabled == false && userDepartmentRefRepository.countByUserIdAndEnabled(current.userId, true) <= 1L) {
                throw AppException(ErrorCode.BAD_REQUEST, "用户至少需要一个启用的关联部门")
            }
        }
        // 保存数据
        userDepartmentRefRepository.save(current)
        return Tips("用户部门关联更新成功")
    }

    private fun validateDepartmentDate(startDate: LocalDate?, endDate: LocalDate?) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw AppException(ErrorCode.BAD_REQUEST, "结束日期不能早于开始日期")
        }
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
        // delete user_profile
        val count2 = userProfileRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] profile [count: {}]", operateUser, user.username, count2)
        // delete user_profile_education
        val count3 = userProfileEducationRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] education [count: {}]", operateUser, user.username, count3)
        // delete user_profile_work
        val count4 = userProfileWorkRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] work [count: {}]", operateUser, user.username, count4)
        // delete user_profile_relationship
        val count5 = userProfileRelationshipRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] relationship [count: {}]", operateUser, user.username, count5)
        // delete user_profile_material
        val count6 = userProfileMaterialRepository.deleteByUserId(user.id!!)
        log.info("user: [{}] operate: delete user [{}] material [count: {}]", operateUser, user.username, count6)
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