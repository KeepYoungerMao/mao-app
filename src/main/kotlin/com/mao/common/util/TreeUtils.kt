package com.mao.common.util

import com.mao.common.entity.Tree

object TreeUtils {

    /**
     * 将扁平节点集合构建为森林，父节点不存在、pid 为 null 或 0 的节点作为根节点
     */
    @JvmStatic
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

}