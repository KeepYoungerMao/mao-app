package com.mao.controller

import com.mao.entity.PageResponse
import com.mao.entity.query.IdQo
import com.mao.entity.query.UserAddQo
import com.mao.entity.query.UserQo
import com.mao.entity.view.UserVo
import com.mao.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping("page")
    suspend fun searchUsers(@RequestBody request: UserQo): PageResponse<UserVo> = userService.searchUsers(request)

    @PostMapping("detail")
    suspend fun searchUser(@RequestBody request: IdQo<Int>): UserVo = userService.searchUser(request.id)

    @PostMapping("create")
    suspend fun saveUser(@RequestBody request: UserAddQo): UserVo = userService.saveUser(request)

    @PostMapping("update")
    suspend fun updateUser(@RequestBody request: UserAddQo): UserVo = userService.updateUser(request)

}