package com.mao.auth.cache

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 缓存的权限数据类
 */
data class UserAuthCacheData(
    val username: String,
    val enabled: Boolean,
    val expired: Boolean,
    val locked: Boolean,
    val authorities: Collection<GrantedAuthority>,
) {
    companion object {
        fun empty() = UserAuthCacheData(
            username = "",
            enabled = false,
            expired = true,
            locked = true,
            authorities = emptySet()
        )
        fun trans(userDetails: UserDetails): UserAuthCacheData {
            return UserAuthCacheData(
                username = userDetails.username,
                enabled = userDetails.isEnabled,
                expired = !userDetails.isAccountNonExpired,
                locked = !userDetails.isAccountNonLocked,
                authorities = userDetails.authorities,
            )
        }
    }
}
