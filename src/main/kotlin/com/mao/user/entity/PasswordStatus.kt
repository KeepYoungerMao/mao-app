package com.mao.user.entity

/**
 * 密码状态枚举类
 * 0：正常，1：首次需要更改密码，2：密码已更改，3：密码已重置
 */
enum class PasswordStatus(val code: Int) {
    OK(0),
    PASSWORD_UNCHANGE(1),
    PASSWORD_EDIT(2),
    PASSWORD_RESET(3)
}