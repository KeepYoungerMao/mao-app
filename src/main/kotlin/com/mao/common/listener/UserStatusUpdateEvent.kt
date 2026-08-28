package com.mao.common.listener

/**
 * ## 用户状态信息改变事件
 * status目前包括：
 * - disabled： 用户被禁用
 * - locked：用户锁定
 * - expired：用户过期
 * - password_edit：用户密码改变
 * - password_reset：用户密码重置
 * - roles_edit：角色改变
 * - deleted：用户删除
 */
data class UserStatusUpdateEvent(val username: String, val status: UserStatusUpdateType)
