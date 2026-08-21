package com.mao.entity

data class DictTypeVo(val id: Int?, val name: String?, val description: String?)
data class DictItemVo(val id: Int?, val pid: Int?, val name: String?)
data class DictGroupVo(val type: DictTypeVo, val items: List<DictItemVo>)

data class ProvinceCityDistrictVo(val id: Int?, val pid: Int?, val code: String?, val name: String?)
data class IndustryVo(val id: Int?, val pid: Int?, val code: String?, val name: String?, val description: String?)

/** 通用树节点，children 为空时序列化为 []，便于前端直接渲染。 */
data class DictTreeVo<T>(val id: Int?, val pid: Int?, val name: String?, val data: T? = null, val children: List<DictTreeVo<T>> = emptyList())