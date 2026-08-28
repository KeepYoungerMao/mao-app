package com.mao.metric.controller

import com.mao.metric.entity.ServerInfo
import com.mao.metric.service.ServerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/server")
class ServerController(
    private val serverService: ServerService
) {

    @GetMapping("info")
    suspend fun serverInfo(): ServerInfo = serverService.getServerInfo()

}