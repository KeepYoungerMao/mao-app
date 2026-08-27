package com.mao.controller

import com.mao.entity.*
import com.mao.extension.OperationLog
import com.mao.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
@OperationLog(module = OperationModule.USER)
class UserController(
    private val userService: UserService
) {

    @PostMapping("page")
    @OperationLog(operation = Operation.PAGE)
    suspend fun searchUsers(@RequestBody request: UserQo): PageResponse<UserVo> = userService.searchUsers(request)

    @PostMapping("detail")
    @OperationLog(operation = Operation.DETAIL)
    suspend fun searchUser(@Valid @RequestBody request: IdQo<Int>): UserDetailVo = userService.searchUser(request.id)

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun saveUser(@Valid @RequestBody request: UserAddQo): UserVo = userService.saveUser(request)

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateUser(@Valid @RequestBody request: UserUpdateQo): UserVo = userService.updateUser(request)

    @PostMapping("password/update")
    @OperationLog(operation = Operation.PASSWORD_UPDATE)
    suspend fun updateUserPassword(@Valid @RequestBody request: UserPasswordUpdateQo): Tips = userService.updateUserPassword(request)

    @PostMapping("password/reset")
    @OperationLog(operation = Operation.PASSWORD_RESET)
    suspend fun resetUserPassword(@Valid @RequestBody request: UserPasswordResetQo): Tips = userService.resetUserPassword(request)

    @PostMapping("renewal")
    @OperationLog(operation = Operation.USER_RENEWAL)
    suspend fun renewUser(@Valid @RequestBody request: UserRenewalQo): UserVo = userService.renewUser(request)

    @PostMapping("lock")
    @OperationLog(operation = Operation.USER_LOCKED)
    suspend fun lockUser(@Valid @RequestBody request: IdQo<Int>): UserVo = userService.lockUser(request.id)

    @PostMapping("unlock")
    @OperationLog(operation = Operation.USER_LOCKED)
    suspend fun unlockUser(@Valid @RequestBody request: IdQo<Int>): UserVo = userService.unlockUser(request.id)

    @PostMapping("enable")
    @OperationLog(operation = Operation.USER_ENABLED)
    suspend fun enableUser(@Valid @RequestBody request: IdQo<Int>): UserVo = userService.enableUser(request.id)

    @PostMapping("disable")
    @OperationLog(operation = Operation.USER_ENABLED)
    suspend fun disableUser(@Valid @RequestBody request: IdQo<Int>): UserVo = userService.disableUser(request.id)

    @PostMapping("role/update")
    @OperationLog(operation = Operation.USER_ROLE)
    suspend fun updateUserRole(@Valid @RequestBody request: UserRoleUpdateQo): Tips = userService.updateUserRole(request)

    @PostMapping("profile/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.USER_ROLE)
    suspend fun updateUserProfile(@Valid @RequestBody request: UserProfileUpdateQo): UserProfileVo = userService.updateUserProfile(request)
    
    @PostMapping("profile/education/all")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.ALL)
    suspend fun searchUserProfileEducations(@Valid @RequestBody request: IdQo<Int>): List<UserProfileEducationVo> =
        userService.searchUserProfileEducations(request.id)
    
    @PostMapping("profile/education/create")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.CREATE)
    suspend fun createUserProfileEducation(@Valid @RequestBody request: UserProfileEducationAddQo): UserProfileEducationVo =
        userService.createUserProfileEducation(request)
    
    @PostMapping("profile/education/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.UPDATE)
    suspend fun updateUserProfileEducation(@Valid @RequestBody request: UserProfileEducationUpdateQo): UserProfileEducationVo =
        userService.updateUserProfileEducation(request)
    
    @PostMapping("profile/education/delete")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.DELETE)
    suspend fun deleteUserProfileEducation(@Valid @RequestBody request: IdQo<Int>): Tips =
        userService.deleteUserProfileEducation(request.id)

    @PostMapping("profile/work/all")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.ALL)
    suspend fun searchUserProfileWorks(@Valid @RequestBody request: IdQo<Int>): List<UserProfileWorkVo> =
        userService.searchUserProfileWorks(request.id)

    @PostMapping("profile/work/create")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.CREATE)
    suspend fun createUserProfileWork(@Valid @RequestBody request: UserProfileWorkAddQo): UserProfileWorkVo =
        userService.createUserProfileWork(request)

    @PostMapping("profile/work/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.UPDATE)
    suspend fun updateUserProfileWork(@Valid @RequestBody request: UserProfileWorkUpdateQo): UserProfileWorkVo =
        userService.updateUserProfileWork(request)

    @PostMapping("profile/work/delete")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.DELETE)
    suspend fun deleteUserProfileWork(@Valid @RequestBody request: IdQo<Int>): Tips =
        userService.deleteUserProfileWork(request.id)

    @PostMapping("profile/relationship/all")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.ALL)
    suspend fun searchUserProfileRelationships(
        @Valid @RequestBody request: IdQo<Int>
    ): List<UserProfileRelationshipVo> = userService.searchUserProfileRelationships(request.id)

    @PostMapping("profile/relationship/create")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.CREATE)
    suspend fun createUserProfileRelationship(
        @Valid @RequestBody request: UserProfileRelationshipAddQo
    ): UserProfileRelationshipVo = userService.createUserProfileRelationship(request)

    @PostMapping("profile/relationship/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.UPDATE)
    suspend fun updateUserProfileRelationship(
        @Valid @RequestBody request: UserProfileRelationshipUpdateQo
    ): UserProfileRelationshipVo = userService.updateUserProfileRelationship(request)

    @PostMapping("profile/relationship/delete")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.DELETE)
    suspend fun deleteUserProfileRelationship(@Valid @RequestBody request: IdQo<Int>): Tips =
        userService.deleteUserProfileRelationship(request.id)

    @PostMapping("profile/material/all")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.ALL)
    suspend fun searchUserProfileMaterials(@Valid @RequestBody request: IdQo<Int>): List<UserProfileMaterialVo> =
        userService.searchUserProfileMaterials(request.id)

    @PostMapping("profile/material/create")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.CREATE)
    suspend fun createUserProfileMaterial(
        @Valid @RequestBody request: UserProfileMaterialAddQo
    ): UserProfileMaterialVo = userService.createUserProfileMaterial(request)

    @PostMapping("profile/material/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.UPDATE)
    suspend fun updateUserProfileMaterial(
        @Valid @RequestBody request: UserProfileMaterialUpdateQo
    ): UserProfileMaterialVo = userService.updateUserProfileMaterial(request)

    @PostMapping("profile/material/delete")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.DELETE)
    suspend fun deleteUserProfileMaterial(@Valid @RequestBody request: IdQo<Int>): Tips =
        userService.deleteUserProfileMaterial(request.id)
        
    @PostMapping("department/create")
    @OperationLog(operation = Operation.USER_DEPARTMENT)
    suspend fun createUserDepartment(@Valid @RequestBody request: UserDepartmentAddQo): Tips =
        userService.createUserDepartment(request)

    @PostMapping("department/update")
    @OperationLog(operation = Operation.USER_DEPARTMENT)
    suspend fun updateUserDepartment(@Valid @RequestBody request: UserDepartmentUpdateQo): Tips =
        userService.updateUserDepartment(request)

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteUser(@Valid @RequestBody request: IdQo<Int>): Tips = userService.deleteUser(request.id)

}