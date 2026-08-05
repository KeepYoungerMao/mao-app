package com.mao.entity.query

import com.mao.extension.QueryField

data class OperationLogQo(
    @QueryField
    var username: String? = null,
    @QueryField
    var module: String? = null,
    @QueryField
    var operation: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    var ip: String? = null,
    @QueryField
    var success: Boolean? = null
) : PageQo()
