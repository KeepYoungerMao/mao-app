package com.mao.common.entity

/**
 * ## 数据状态字段枚举
 * - 0 禁用
 * - 1 启用
 * - 2 归档
 *
 * 1. 该数据状态字段适用于本项目任何需要表示状态的字段，并且数据库表示状态的字段应该遵守该协议。
 * 2. 如果仅包含禁用和启用，可以不用归档类型，但也必须遵循0-禁用，1-启用的映射
 *
 * 当前适用：字典细项、部门
 */
enum class DataStatus(val status: Int) {

    /**
     * 禁用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(1),

    /**
     * 归档
     */
    ARCHIVED(2),

}