package com.mao.role.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_role")
data class RoleDo(
    @Id
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null
) : BaseDo()
