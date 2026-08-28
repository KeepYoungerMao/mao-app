package com.mao.auth.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.mao.common.config.JwtConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import java.util.concurrent.TimeUnit

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