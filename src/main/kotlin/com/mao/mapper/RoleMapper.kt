package com.mao.mapper

import com.mao.entity.RoleDo
import com.mao.entity.RoleVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class RoleMapper {

    fun toVo(roleDo: RoleDo): RoleVo = RoleViewMapper.map(roleDo)
}

object RoleViewMapper : ObjectMappie<RoleDo, RoleVo>() {
    override fun map(from: RoleDo): RoleVo = mapping {}
}