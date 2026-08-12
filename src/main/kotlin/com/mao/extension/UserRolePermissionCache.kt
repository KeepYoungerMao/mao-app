package com.mao.extension

import com.github.benmanes.caffeine.cache.Caffeine
import com.mao.config.JwtConfig
import com.mao.repository.RolePermissionRefRepository
import com.mao.repository.UserRoleRefRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

/**
 * 缓存的权限数据类
 */
data class RolePermissionData(val username: String, val roles: List<String>, val permissions: List<String>)

/**
 * 角色权限信息的缓存
 */
interface UserRolePermissionCache {
    suspend fun cache(rolePermissionData: RolePermissionData)
    suspend fun get(username: String?): RolePermissionData?
    suspend fun evict(username: String)
    suspend fun evict(usernames: List<String>)
    suspend fun evictByRole(roleId: Int)
}

/**
 * 本地缓存方式
 * 使用Caffeine
 */
class CaffeineUserRolePermissionCache(
    jwtConfig: JwtConfig,
    private val userRoleRefRepository: UserRoleRefRepository,
    private val rolePermissionRefRepository: RolePermissionRefRepository
) : UserRolePermissionCache {

    private val cache = Caffeine.newBuilder()
        .expireAfterWrite(jwtConfig.accessTokenExpiration, TimeUnit.MILLISECONDS)
        .build<String, RolePermissionData>()
    private val mutex = Mutex()

    override suspend fun cache(rolePermissionData: RolePermissionData) {
        cache.put(rolePermissionData.username, rolePermissionData)
    }

    override suspend fun get(username: String?): RolePermissionData? {
        if (username == null) return null
        // 缓存命中
        cache.getIfPresent(username)?.let { return it }
        // 缓存未命中，加锁查询数据库
        return mutex.withLock {
            // 双重检查：可能其他协程已填充
            cache.getIfPresent(username)?.let { return@withLock it }
            val data = queryFromDb(username)
            if (data != null) {
                cache(data)
            }
            data
        }
    }

    private suspend fun queryFromDb(username: String): RolePermissionData? {
        // 查询用户拥有的角色
        val userRoles = userRoleRefRepository.getRoleByUsername(username)
            .filterNotNull()
            .filter { it.id != null && it.name != null }.toList()
        if (userRoles.isEmpty()) return null
        val roles = userRoles.mapNotNull { it.name }
        val roleIds = userRoles.mapNotNull { it.id }
        // 查询角色对应的权限
        val permissions = rolePermissionRefRepository.getPermissionByRoleIdIn(roleIds)
            .filterNotNull().filter { it.isNotBlank() }.toList()
        // 构建缓存
        return RolePermissionData(
            username = username,
            roles = roles,
            permissions = permissions
        )
    }

    override suspend fun evict(username: String) {
        cache.invalidate(username)
    }

    override suspend fun evict(usernames: List<String>) {
        cache.invalidateAll(usernames)
    }

    override suspend fun evictByRole(roleId: Int) {
        val usernames = userRoleRefRepository.getUsernameByRoleId(roleId).toList()
        evict(usernames)
    }

}