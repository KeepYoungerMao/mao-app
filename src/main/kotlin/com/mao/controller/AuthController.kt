package com.mao.controller

import com.mao.entity.auth.LoginRequest
import com.mao.entity.auth.RefreshRequest
import com.mao.entity.auth.RsaKey
import com.mao.entity.auth.TokenResponse
import com.mao.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("key")
    suspend fun key(): RsaKey = authService.key()

    @PostMapping("token")
    suspend fun getToken(@RequestBody request: LoginRequest): TokenResponse = authService.login(request)

    @PostMapping("token/refresh")
    suspend fun refreshToken(@RequestBody request: RefreshRequest): TokenResponse = authService.refreshToken(request)

}