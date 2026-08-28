package com.mao.dict.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sys_dict_item")
data class DictItemDo(
    @Id
    var id: Int? = null,
    var pid: Int? = null,
    var name: String? = null,
    var status: Int? = null
) : BaseDo()
