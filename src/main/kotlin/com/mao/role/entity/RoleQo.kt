package com.mao.role.entity

import com.mao.common.entity.PageQo
import com.mao.common.repository.QueryField

data class RoleQo(
    @QueryField(type = QueryField.Type.LIKE)
    val name: String? = null
) : PageQo()
