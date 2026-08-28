package com.mao.auth.controller

import com.mao.auth.entity.RefreshRequest
import com.mao.auth.entity.RsaKeyData
import com.mao.auth.entity.TokenRequest
import com.mao.auth.entity.TokenResponse
import com.mao.auth.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @GetMapping("key")
    suspend fun key(): RsaKeyData = authService.key()

    @PostMapping("token")
    suspend fun getToken(@RequestBody request: TokenRequest): TokenResponse = authService.login(request)

    @PostMapping("token/refresh")
    suspend fun refreshToken(@RequestBody request: RefreshRequest): TokenResponse = authService.refreshToken(request)

}