package com.mao.extension

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.mao.config.JwtConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import java.util.concurrent.TimeUnit

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

/**
 * 角色权限信息的缓存
 */
interface UserAuthCache {

    /**
     * ## 获取用户的权限信息，包括拥有的角色和拥有的权限
     * 优先从缓存中获取，若缓存中不存在，则需查询数据库再存入缓存。
     * 如果用户不存在则返回null
     * @param username 用户名
     * @return 用户角色权限信息
     */
    suspend fun get(username: String?): UserAuthCacheData?

    /**
     * ## 删除缓存
     * 删除指定用户的角色权限缓存数据。
     * 1. 用户登出后，调用此方法删除缓存数据
     * 2. 用户更改角色，调用此方法删除缓存数据
     * @param username 用户名
     */
    suspend fun evict(username: String)

    /**
     * ## 删除多个用户缓存
     * 删除指定的多个用户的角色权限缓存数据
     * 1. 当角色变更权限时，与该角色绑定的用户均需删除缓存数据
     */
    suspend fun evict(usernames: List<String>)

    fun getTotal(): Int

}

/**
 * 本地缓存方式
 * 使用Caffeine
 */
class CaffeineUserAuthCache(
    jwtConfig: JwtConfig,
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
) : UserAuthCache {

    // 独立的作用域，用于在 Cache 未命中时启动查库协程
    private val cacheScope = CoroutineScope(Dispatchers.IO)

    // 使用 AsyncCache 契合 WebFlux，不仅非阻塞，还能天然防止缓存击穿 (并发请求同一 Key 时只查一次库)
    private val cache: AsyncCache<String, UserAuthCacheData?> = Caffeine.newBuilder()
        // 设置有效时长为token有效期
        // 设置的是只要数据被获取，会重新刷新有效期。（对比于expireAfterWrite不会刷新，而是固定有效期）
        .expireAfterAccess(jwtConfig.accessTokenExpiration, TimeUnit.MILLISECONDS)
        // 缓存最大量，根据实际用户量调整
        .maximumSize(10_000)
        .buildAsync()

    override suspend fun get(username: String?): UserAuthCacheData? {
        if (username.isNullOrBlank()) return null
        // AsyncCache.get 接收一个 BiFunction。
        // 当缓存未命中时，只会有一个线程/协程进入此代码块执行查库，其他并发请求会挂起等待这个 Future 完成
        return cache.get(username) { key, _ ->
            cacheScope.future {
                val userDetails = reactiveUserDetailsService.findByUsername(key).awaitSingleOrNull() ?: return@future null
                UserAuthCacheData.trans(userDetails)
            }
        }.await()
    }

    override suspend fun evict(username: String) {
        // 获取同步视图以执行删除
        cache.synchronous().invalidate(username)
    }

    override suspend fun evict(usernames: List<String>) {
        // 获取同步视图以执行删除
        cache.synchronous().invalidateAll(usernames)
    }

    override fun getTotal(): Int {
        return cache.asMap().keys.size
    }

}