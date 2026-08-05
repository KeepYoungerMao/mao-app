package com.mao.entity.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_user")
data class UserDo(

    // 主键 id：对于数据库自增主键，默认设为 null。
    // R2DBC 会根据 id 是否为 null 来决定执行 INSERT 还是 UPDATE。
    @Id
    var id: Int? = null,

    // 用户名
    var username: String? = null,

    // 密码
    var password: String? = null,

    // 头像地址 (可空，且提供默认值为 null)
    var avatar: String? = null,

    // 手机号 (可空，默认值为 null)
    var phone: String? = null,

    // 邮箱 (可空，默认值为 null)
    var email: String? = null,

    // 是否过期 (默认为 false)
    var expired: Boolean? = null,

    // 是否锁定 (默认为 false)
    var locked: Boolean? = null,

    // 是否可使用 (默认为 false，表示第一次创建)
    var enabled: Boolean? = null,

    // 过期时间 (毫秒级时间戳，可空)
    var expireTime: Long? = null,

    // 上次登陆时间 (可空)
    var lastLoginTime: LocalDateTime? = null,

    // 是否需要修改密码（默认为false）
    var mustChangePassword: Boolean? = null,

) : BaseDo()