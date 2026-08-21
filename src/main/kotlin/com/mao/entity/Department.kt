package com.mao.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_department")
data class DepartmentDo(@Id var id: Int? = null, var pid: Int? = null, var departmentName: String? = null, var description: String? = null) : BaseDo()

data class DepartmentVo(val id: Int? = null, val pid: Int? = null, val departmentName: String? = null, val description: String? = null) : BaseVo()
