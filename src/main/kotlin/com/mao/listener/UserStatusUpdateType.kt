package com.mao.listener

enum class UserStatusUpdateType {

    // 用户被禁用
    DISABLED,

    // 用户锁定
    LOCKED,

    // 用户过期
    EXPIRED,

    // 用户密码改变
    PASSWORD_EDIT,

    // 用户密码重置
    PASSWORD_RESET,

    // 角色改变
    ROLES_EDIT,

    // 用户删除
    DELETED

}