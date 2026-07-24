package com.mao.entity.query

import com.mao.extension.QueryField

data class RoleQo(
    @QueryField(type = QueryField.Type.LIKE)
    val name: String? = null,
) : PageQo()
