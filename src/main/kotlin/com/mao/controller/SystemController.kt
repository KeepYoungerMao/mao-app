package com.mao.controller

import com.mao.entity.ServerInfo
import com.mao.service.SystemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/server")
class SystemController(
    private val systemService: SystemService
) {

    @GetMapping("info")
    suspend fun serverInfo(): ServerInfo = systemService.getServerInfo()

}