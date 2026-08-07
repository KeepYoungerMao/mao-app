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
    private val jwtConfig: JwtConfig
) {

    suspend fun key(): RsaKey = RsaKey(jwtService.getPublicKey())

    suspend fun login(request: LoginRequest): TokenResponse {
        // 前置检查入参
        val username = request.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 提取出真正的明文密码
        val rawPassword = passwordHandler.decryptPassword(request.password, request.timestamp)

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

    private suspend fun createToken(username: String, password: String? = null): TokenResponse {
        // 将 Mono<UserDetails> 挂起并解包为 UserDetails?
        val userDetails = userDetailsService.findByUsername(username).awaitSingleOrNull()
            ?: throw AppException(ErrorCode.USER_NOT_FOUND) // 查不到用户时抛出

        // 3. 账号状态校验（卫语句风格平铺，清晰直观）
        if (!userDetails.isEnabled) {
            throw AppException(ErrorCode.USER_UNENABLED)
        }
        if (!userDetails.isAccountNonExpired) {
            throw AppException(ErrorCode.USER_EXPIRED)
        }
        if (!userDetails.isAccountNonLocked) {
            throw AppException(ErrorCode.USER_LOCKED)
        }

        // 4. 密码校验
        if (password != null) {
            if (!passwordEncoder.matches(password, userDetails.password)) {
                throw AppException(ErrorCode.PASSWORD_ERROR)
            }
        }

        // 5. 签发 Token 并返回
        val accessToken = jwtService.generateAccessToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration / 1000
        )
    }

}