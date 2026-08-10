package com.mao.entity

import com.mao.ex.AppException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

/**
 * 实现spring security的UserDetails的用户数据
 */
data class AuthUserDetails(
    private val username: String,
    private val password: String?,
    private val expired: Boolean,
    private val locked: Boolean,
    private val enabled: Boolean,
    private val mustChangePassword: Boolean,
    private val authorities: Collection<GrantedAuthority>
): UserDetails, Serializable {

    companion object {
        fun create(userDo: UserDo): AuthUserDetails {
            return AuthUserDetails(
                username = userDo.username ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                password = userDo.password ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                expired = userDo.expired ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                locked = userDo.locked ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                enabled = userDo.enabled ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                mustChangePassword = userDo.mustChangePassword ?: throw AppException(ErrorCode.USER_NOT_FOUND),
                authorities = emptySet(),
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getUsername(): String = username
    override fun getPassword(): String? = password
    override fun isAccountNonExpired(): Boolean = !expired
    override fun isAccountNonLocked(): Boolean = !locked
    override fun isCredentialsNonExpired(): Boolean = !expired
    override fun isEnabled(): Boolean = enabled
}

/**
 * 获取token请求参数
 */
data class LoginRequest(val username: String?, val password: String?, val timestamp: Long?)

/**
 * 刷新token请求参数
 */
data class RefreshRequest(val refreshToken: String?)

/**
 * 获取公钥响应结果
 */
data class RsaKey(val publicKey: String)

/**
 * token响应结果
 */
data class TokenResponse(val accessToken: String, val refreshToken: String, val expiresIn: Long, val tokenType: String= "Bearer")