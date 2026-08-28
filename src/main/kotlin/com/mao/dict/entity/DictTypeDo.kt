package com.mao.dict.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_dict")
data class DictTypeDo(
    @Id
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null
)
