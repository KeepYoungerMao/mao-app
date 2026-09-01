package com.mao.dict.entity

import com.mao.common.entity.BaseVo

data class DictItemVo(
    val id: Int?,
    val pid: Int?,
    val name: String?,
    val status: Int? = null
) : BaseVo()