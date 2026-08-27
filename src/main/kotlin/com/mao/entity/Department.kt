package com.mao.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sys_department")
data class DepartmentDo(
    @Id var id: Int? = null,
    var parentId: Int? = null,
    var departmentCode: String? = null,
    var departmentName: String? = null,
    var description: String? = null,
    var departmentType: Int? = null,
    var memberAssignable: Boolean? = null,
    var sortOrder: Int? = null,
    var status: Int? = null,
    var creator: String? = null,
    var createTime: LocalDateTime? = null,
    var updater: String? = null,
    var updateTime: LocalDateTime? = null
)

data class DepartmentVo(
    val id: Int? = null,
    val parentId: Int? = null,
    val departmentCode: String? = null,
    val departmentName: String? = null,
    val description: String? = null,
    val departmentType: Int? = null,
    val memberAssignable: Boolean? = null,
    val sortOrder: Int? = null,
    val status: Int? = null
) : BaseVo()
