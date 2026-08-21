package com.mao.entity

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/** 数据库公共字段类。 */
open class BaseDo {
    var deleted: Boolean? = null
    @org.springframework.data.annotation.CreatedBy
    var creator: String? = null
    @org.springframework.data.annotation.CreatedDate
    var createTime: LocalDateTime? = null
    @org.springframework.data.annotation.LastModifiedBy
    var updater: String? = null
    @org.springframework.data.annotation.LastModifiedDate
    var updateTime: LocalDateTime? = null
}

/** 查询结果数据公共字段类。 */
open class BaseVo {
    var creator: String? = null
    var createTime: LocalDateTime? = null
    var updater: String? = null
    var updateTime: LocalDateTime? = null
}

data class IdQo<T>(
    @field:NotNull(message = "请传递数据ID以操作")
    val id: T?
)

/** 分页查询参数。 */
open class PageQo {
    var pageNum: Int = 1
    var pageSize: Int = 10
    var recount: Boolean = true
    var lastHitCount: Long? = null
    fun offset(): Int = (pageNum - 1) * pageSize
}

/**
 * 树节点约定。具体 VO 保留自身业务字段，只需声明节点关系和子节点复制方式。
 */
interface Tree<T : Tree<T>> {
    val id: Int?
    val pid: Int?
    val children: List<T>

    fun withChildren(children: List<T>): T
}

/** 将扁平节点集合构建为森林，父节点不存在、pid 为 null 或 0 的节点作为根节点。 */
fun <T : Tree<T>> buildTree(nodes: List<T>): List<T> {
    val ids = nodes.mapNotNull(Tree<T>::id)
    require(ids.size == ids.toSet().size) { "树节点 ID 不能重复" }
    val nodeIds = ids.toSet()
    val childrenByPid = nodes.groupBy(Tree<T>::pid)
    val visitedIds = mutableSetOf<Int>()

    fun buildBranch(node: T, ancestors: Set<Int>): T {
        val nodeId = node.id ?: return node.withChildren(emptyList())
        require(nodeId !in ancestors) { "树节点存在循环引用: $nodeId" }
        visitedIds += nodeId
        val children = childrenByPid[nodeId].orEmpty()
            .map { buildBranch(it, ancestors + nodeId) }
        return node.withChildren(children)
    }

    val roots = nodes
        .filter { it.pid == null || it.pid == 0 || it.pid !in nodeIds }
        .map { buildBranch(it, emptySet()) }
    require(visitedIds.containsAll(nodeIds)) { "树节点存在循环引用" }
    return roots
}
