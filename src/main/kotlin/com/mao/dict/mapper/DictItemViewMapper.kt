package com.mao.dict.mapper

import com.mao.dict.entity.DictItemDo
import com.mao.dict.entity.DictItemVo
import tech.mappie.api.ObjectMappie

object DictItemViewMapper : ObjectMappie<DictItemDo, DictItemVo>() {

    override fun map(from: DictItemDo): DictItemVo = mapping {  }

}