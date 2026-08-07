package com.mao.controller

import com.mao.entity.LoginRequest
import com.mao.entity.RefreshRequest
import com.mao.entity.RsaKey
import com.mao.entity.TokenResponse
import com.mao.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @GetMapping("key")
    suspend fun key(): RsaKey = authService.key()

    @PostMapping("token")
    suspend fun getToken(@RequestBody request: LoginRequest): TokenResponse = authService.login(request)

    @PostMapping("token/refresh")
    suspend fun refreshToken(@RequestBody request: RefreshRequest): TokenResponse = authService.refreshToken(request)

}