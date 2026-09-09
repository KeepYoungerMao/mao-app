package com.mao.department.entity

/**
 * ## 部门类型
 * - 1 一级部门
 * - 2 二级部门
 * - 3 三级部门
 * - 4 四级部门
 * - 5 五级部门
 *
 * 公司为最大类型部门，虚拟组织为最小类型部门
 */
enum class DepartmentType(val code: Int) {

    FIRST(1),

    SECOND(2),

    THIRD(3),

    FOURTH(4),

    FIFTH(5),

}