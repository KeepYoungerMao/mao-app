package com.mao.dict.mapper

import com.mao.dict.entity.DictTypeDo
import com.mao.dict.entity.DictTypeVo
import tech.mappie.api.ObjectMappie

object DictTypeViewMapper : ObjectMappie<DictTypeDo, DictTypeVo>() {

    override fun map(from: DictTypeDo): DictTypeVo = mapping {  }

}