package com.mao.entity.query

/**
 * # 用户数据新增/更新包装类
 * 1. 当用户新增，id不传
 * 2. 当用户更新，传递id字段
 * 3. 用户新增或更新都传递全量数据，R2dbcRepository执行的是全量新增/更新策略
 * 4. lastLoginTime由登录系统赋值，不在新增/更新结构中
 * 5. createBy，createTime，updateBy，updateTime字段由审计功能赋值，不在新增/更新结构中
 */
data class UserAddQo(
    val id: Int? = null,
    val username: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val expired: Boolean? = null,
    val locked: Boolean? = null,
    val enabled: Boolean? = null,
    val expireTime: Long? = null,
)
