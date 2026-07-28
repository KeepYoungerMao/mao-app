package com.mao.service

import com.mao.config.JwtConfig
import com.mao.entity.ErrorCode
import com.mao.entity.auth.LoginRequest
import com.mao.entity.auth.RefreshRequest
import com.mao.entity.auth.RsaKey
import com.mao.entity.auth.TokenResponse
import com.mao.ex.AppException
import com.mao.extension.JwtService
import com.mao.util.RsaUtils
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class AuthService(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val jwtConfig: JwtConfig
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun key(): RsaKey = RsaKey(jwtService.getPublicKey())

    suspend fun login(request: LoginRequest): TokenResponse {
        // 前置检查入参
        val username = request.username ?: throw AppException(ErrorCode.BAD_REQUEST)
        val password = request.password ?: throw AppException(ErrorCode.BAD_REQUEST)
        val timestamp = request.timestamp ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 防止重放攻击，校验时间戳，允许30秒窗口期
        val now = System.currentTimeMillis()
        if (abs(now - timestamp) > 30000) {
            throw AppException(ErrorCode.AUTHENTICATION_TIMEOUT)
        }
        // 解密
        val privateKey = jwtService.getPrivateKey()
        val rawPasswordWithTimestamp = try {
            RsaUtils.decrypt(password, privateKey)
        } catch (e: Exception) {
            log.error("Error decrypting password: ", e)
            throw AppException(ErrorCode.BAD_AUTHENTICATION_REQUEST)
        }
        // 检验密码格式是否正确
        if (!rawPasswordWithTimestamp.endsWith(":$timestamp")) {
            throw AppException(ErrorCode.BAD_REQUEST)
        }
        // 提取出真正的明文密码
        val rawPassword = rawPasswordWithTimestamp.removeSuffix(":$timestamp")

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