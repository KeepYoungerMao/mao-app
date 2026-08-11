package com.mao.controller

import com.mao.entity.*
import com.mao.extension.OperationLog
import com.mao.service.UserService
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
    suspend fun searchUser(@RequestBody request: IdQo<Int>): UserVo = userService.searchUser(request.id)

    @PostMapping("create")
    @OperationLog(operation = Operation.CREATE)
    suspend fun saveUser(@RequestBody request: UserAddQo): UserVo = userService.saveUser(request)

    @PostMapping("update")
    @OperationLog(operation = Operation.UPDATE)
    suspend fun updateUser(@RequestBody request: UserUpdateQo): UserVo = userService.updateUser(request)

    @PostMapping("password/update")
    @OperationLog(operation = Operation.PASSWORD_UPDATE)
    suspend fun updateUserPassword(@RequestBody request: UserPasswordUpdateQo): Tips = userService.updateUserPassword(request)

    @PostMapping("password/reset")
    @OperationLog(operation = Operation.PASSWORD_RESET)
    suspend fun resetUserPassword(@RequestBody request: UserPasswordResetQo): Tips = userService.resetUserPassword(request)

    @PostMapping("renewal")
    @OperationLog(operation = Operation.USER_RENEWAL)
    suspend fun renewUser(@RequestBody request: UserRenewalQo): UserVo = userService.renewUser(request)

    @PostMapping("lock")
    @OperationLog(operation = Operation.USER_LOCKED)
    suspend fun lockUser(@RequestBody request: IdQo<Int>): UserVo = userService.lockUser(request.id)

    @PostMapping("unlock")
    @OperationLog(operation = Operation.USER_LOCKED)
    suspend fun unlockUser(@RequestBody request: IdQo<Int>): UserVo = userService.unlockUser(request.id)

    @PostMapping("enable")
    @OperationLog(operation = Operation.USER_ENABLED)
    suspend fun enableUser(@RequestBody request: IdQo<Int>): UserVo = userService.enableUser(request.id)

    @PostMapping("disable")
    @OperationLog(operation = Operation.USER_ENABLED)
    suspend fun disableUser(@RequestBody request: IdQo<Int>): UserVo = userService.disableUser(request.id)

    @PostMapping("role/update")
    @OperationLog(operation = Operation.USER_ROLE)
    suspend fun updateUserRole(@RequestBody request: UserRoleUpdateQo): Tips = userService.updateUserRole(request)

    @PostMapping("profile/update")
    @OperationLog(module = OperationModule.USER_PROFILE, operation = Operation.USER_ROLE)
    suspend fun updateUserProfile(@RequestBody request: UserProfileUpdateQo): UserProfileVo = userService.updateUserProfile(request)

    @PostMapping("delete")
    @OperationLog(operation = Operation.DELETE)
    suspend fun deleteUser(@RequestBody request: IdQo<Int>): Tips = userService.deleteUser(request.id)

}