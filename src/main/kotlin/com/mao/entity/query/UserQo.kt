package com.mao.entity.query

import com.mao.extension.QueryField

data class UserQo(

    @QueryField(type = QueryField.Type.LIKE)
    val username: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val phone: String? = null,
    @QueryField(type = QueryField.Type.LIKE)
    val email: String? = null,
    @QueryField
    val expired: Boolean? = null,
    @QueryField
    val locked: Boolean? = null,
    @QueryField
    val enabled: Boolean? = null

) : PageQo()