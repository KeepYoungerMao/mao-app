package com.mao.auth.service

import com.mao.auth.entity.*
import com.mao.auth.handler.PasswordHandler
import com.mao.common.config.JwtConfig
import com.mao.common.entity.ErrorCode
import com.mao.common.ex.AppException
import com.mao.metric.service.ServerService
import com.mao.user.entity.PasswordStatus
import com.mao.user.service.UserService
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
    private val userService: UserService,
    private val serverService: ServerService
) {

    suspend fun key(): RsaKeyData = RsaKeyData(jwtService.getPublicKey())

    suspend fun login(request: TokenRequest): TokenResponse {
        // 前置检查入参
        val username = request.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 提取出真正的明文密码
        val rawPassword = passwordHandler.decryptPassword(request.password, request.timestamp)
        // 创建token
        return createToken(username, rawPassword, false)
    }

    suspend fun refreshToken(request: RefreshRequest): TokenResponse {
        val refreshToken = request.refreshToken ?: throw AppException(ErrorCode.BAD_REQUEST)
        val claims = jwtService.validateToken(refreshToken)
        if (claims.getStringClaim("type") != "refresh") {
            throw AppException(ErrorCode.INVALID_TOKEN)
        }
        return createToken(claims.subject, null, true)
    }

    /**
     * ## 创建token
     * 登录创建token与刷新token逻辑一致，都需要获取一遍用户信息
     */
    private suspend fun createToken(username: String, password: String? = null, refresh: Boolean): TokenResponse {
        // 获取用户信息，并转为AuthUserDetails
        val userDetails = userDetailsService.findByUsername(username).awaitSingleOrNull()
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val authUserDetails = userDetails as AuthUserDetails

        // 账号状态校验
        if (!authUserDetails.isEnabled) {
            throw AppException(ErrorCode.USER_DISABLED)
        }
        if (!authUserDetails.isAccountNonExpired) {
            throw AppException(ErrorCode.USER_EXPIRED)
        }
        if (!authUserDetails.isAccountNonLocked) {
            throw AppException(ErrorCode.USER_LOCKED)
        }
        // 密码状态，0：正常，1：首次需要更改密码，2：密码已更改，3：密码已重置
        // 当首次注册未更改密码、用户密码更改、密码重置后，后台会将passwordStatus设置为非0特殊状态，
        // 此时通过刷新token来获取token不能通过，必须通过创建token来获取
        // 当用户通过创建token方式登录成功后，如果passwordStatus状态不为0，将更新password为0
        // 后续用户可以正常通过refresh方式获取token
        if (refresh) {
            when (authUserDetails.passwordStatus) {
                PasswordStatus.PASSWORD_UNCHANGE.code -> throw AppException(ErrorCode.UNCHANGE_PASSWORD)
                PasswordStatus.PASSWORD_EDIT.code -> throw AppException(ErrorCode.PASSWORD_EDIT)
                PasswordStatus.PASSWORD_RESET.code -> throw AppException(ErrorCode.PASSWORD_RESET)
            }
        } else {
            if (authUserDetails.passwordStatus != PasswordStatus.OK.code) {
                userService.resetUserPasswordStatus(authUserDetails.id)
            }
        }

        // 密码校验
        if (password != null) {
            if (!passwordEncoder.matches(password, authUserDetails.password)) {
                throw AppException(ErrorCode.PASSWORD_ERROR)
            }
        }

        // 签发 Token 并返回
        val accessToken = jwtService.generateAccessToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        // 记录登录
        serverService.recordLogin(authUserDetails.id)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration / 1000
        )
    }

}