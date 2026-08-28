package com.mao.common.entity

/**
 * 树节点约定。具体 VO 保留自身业务字段，只需声明节点关系和子节点复制方式。
 */
interface Tree<T : Tree<T>> {
    val id: Int?
    val pid: Int?
    val children: List<T>

    fun withChildren(children: List<T>): T
}