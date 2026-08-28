package com.mao.common.repository

import org.springframework.data.relational.core.query.Criteria
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

/**
 * 数据查询构建工具
 * 基于 QueryField 注解构建 Criteria
 */
object QueryBuilder {

    /**
     * 缓存每个有效查询字段的元数据
     */
    data class QueryFieldMeta(val field: Field, val columnName: String, val matchType: QueryField.Type)

    // 核心：元数据高并发缓存池
    private val METADATA_CACHE = ConcurrentHashMap<Class<*>, List<QueryFieldMeta>>()

    /**
     * 动态构建 Criteria
     */
    fun build(queryObj: Any?): Criteria {
        var criteria = Criteria.empty()
        if (queryObj == null) {
            return criteria
        }

        val clazz = queryObj::class.java

        // 1. 从缓存获取或触发解析
        val fieldMetas = METADATA_CACHE.computeIfAbsent(clazz) { parseQueryMetadata(it) }

        // 2. 遍历元数据，无反射性能损耗构建条件
        for (meta in fieldMetas) {
            try {
                val value = meta.field.get(queryObj) ?: continue

                // 空值过滤
                if (value is String && value.isBlank()) {
                    continue
                }

                // 动态拼接 Criteria
                criteria = when (meta.matchType) {
                    QueryField.Type.EQUAL -> criteria.and(meta.columnName).`is`(value)
                    QueryField.Type.LIKE -> criteria.and(meta.columnName).like("%$value%")
                    QueryField.Type.LESS_THAN -> criteria.and(meta.columnName).lessThan(value)
                    QueryField.Type.GREATER_THAN -> criteria.and(meta.columnName).greaterThan(value)
                    QueryField.Type.IN -> {
                        val collection = convertToCollection(value)
                        if (collection.isNotEmpty()) {
                            criteria.and(meta.columnName).`in`(collection)
                        } else {
                            criteria
                        }
                    }
                }
            } catch (_: IllegalAccessException) {
                // 已经 setAccessible(true)，理论上不会发生
            }
        }
        return criteria
    }

    /**
     * 核心解析方法：将一个类及其父类的有效字段解析为元数据列表
     */
    private fun parseQueryMetadata(clazz: Class<*>): List<QueryFieldMeta> {
        val metas = mutableListOf<QueryFieldMeta>()
        var currentClass: Class<*>? = clazz

        while (currentClass != null && currentClass != Any::class.java) {
            for (field in currentClass.declaredFields) {
                val modifiers = field.modifiers
                // 过滤静态和瞬时属性
                if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
                    continue
                }

                // 提前反射解禁
                field.isAccessible = true

                // 检查注解
                if (field.isAnnotationPresent(QueryField::class.java)) {
                    val anno = field.getAnnotation(QueryField::class.java)
                    val columnName = anno.name.ifEmpty { field.name }
                    val matchType = anno.type
                    metas.add(QueryFieldMeta(field, columnName, matchType))
                }
            }
            currentClass = currentClass.superclass
        }
        return metas
    }

    /**
     * 核心智能类型转换器：将输入的值转换为 Collection 集合
     */
    private fun convertToCollection(value: Any?): Collection<*> {
        return when (value) {
            null -> emptyList<Any>()
            // 情况 A：已经是集合
            is Collection<*> -> value
            // 情况 B：对象数组
            is IntArray -> value.toList()
            is LongArray -> value.toList()
            is FloatArray -> value.toList()
            is DoubleArray -> value.toList()
            is BooleanArray -> value.toList()
            is CharArray -> value.toList()
            is ByteArray -> value.toList()
            is ShortArray -> value.toList()
            // 情况 C：逗号分隔的字符串
            is String -> {
                if (value.isNotBlank()) {
                    value.split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                } else {
                    emptyList()
                }
            }
            else -> {
                // 情况 D：基本类型数组的兜底兼容（如 IntArray, LongArray 等）
                if (value.javaClass.isArray) {
                    convertPrimitiveArray(value)
                } else {
                    // 情况 E：单值包装
                    listOf(value)
                }
            }
        }
    }

    /**
     * 兼容基本类型数组转换为包装类集合
     */
    private fun convertPrimitiveArray(array: Any): Collection<*> {
        val length = Array.getLength(array)
        return List(length) { i -> Array.get(array, i) }
    }

}