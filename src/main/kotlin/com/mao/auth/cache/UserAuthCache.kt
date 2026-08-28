package com.mao.auth.cache

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

    /**
     * ## 统计当前缓存数量
     * 用于粗略统计用户在线人数
     */
    fun getTotal(): Int

}