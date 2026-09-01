package com.mao.dict.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_region")
data class RegionDo(
    @Id
    var id: Int? = null,
    var pid: Int? = null,
    var code: String? = null,
    var name: String? = null
) : BaseDo()
