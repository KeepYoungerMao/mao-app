package com.mao.service

import com.mao.config.JwtConfig
import com.mao.entity.*
import com.mao.ex.AppException
import com.mao.extension.JwtService
import com.mao.extension.PasswordHandler
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val passwordHandler: PasswordHandler,
    private val jwtService: JwtService,
    private val jwtConfig: JwtConfig,
    private val systemService: SystemService
) {

    suspend fun key(): RsaKey = RsaKey(jwtService.getPublicKey())

    suspend fun login(request: LoginRequest): TokenResponse {
        // 前置检查入参
        val username = request.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 提取出真正的明文密码
        val rawPassword = passwordHandler.decryptPassword(request.password, request.timestamp)
        // 创建token
        return createToken(username, rawPassword)
    }

    suspend fun refreshToken(request: RefreshRequest): TokenResponse {
        val refreshToken = request.refreshToken ?: throw AppException(ErrorCode.BAD_REQUEST)
        val claims = jwtService.validateToken(refreshToken)
        if (claims.getStringClaim("type") != "refresh") {
            throw AppException(ErrorCode.INVALID_TOKEN)
        }
        return createToken(claims.subject)
    }

    /**
     * ## 创建token
     * 登录创建token与刷新token逻辑一致，都需要获取一遍用户信息
     */
    private suspend fun createToken(username: String, password: String? = null): TokenResponse {
        // 获取用户信息，并转为AuthUserDetails
        val userDetails = userDetailsService.findByUsername(username).awaitSingleOrNull()
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val authUserDetails = userDetails as AuthUserDetails

        // 账号状态校验
        if (!authUserDetails.isEnabled) {
            throw AppException(ErrorCode.USER_UNENABLED)
        }
        if (!authUserDetails.isAccountNonExpired) {
            throw AppException(ErrorCode.USER_EXPIRED)
        }
        if (!authUserDetails.isAccountNonLocked) {
            throw AppException(ErrorCode.USER_LOCKED)
        }
        if (authUserDetails.mustChangePassword) {
            throw AppException(ErrorCode.UNCHANGE_PASSWORD)
        }

        // 密码校验
        if (password != null) {
            if (!passwordEncoder.matches(password, userDetails.password)) {
                throw AppException(ErrorCode.PASSWORD_ERROR)
            }
        }

        // 签发 Token 并返回
        val accessToken = jwtService.generateAccessToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        // 记录登录
        systemService.recordLogin(authUserDetails.id)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration / 1000
        )
    }

}