package com.mao.controller

import com.mao.entity.Operation
import com.mao.entity.OperationModule
import com.mao.entity.PageResponse
import com.mao.entity.query.IdQo
import com.mao.entity.query.UserAddQo
import com.mao.entity.query.UserQo
import com.mao.entity.query.UserUpdateQo
import com.mao.entity.view.UserVo
import com.mao.extension.OperationLog
import com.mao.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
@OperationLog(module = OperationModule.USER)
class UserController(private val userService: UserService) {

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

}