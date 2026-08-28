package com.mao.role.mapper

import com.mao.role.entity.RoleDo
import com.mao.role.entity.RoleVo
import tech.mappie.api.ObjectMappie

object RoleViewMapper : ObjectMappie<RoleDo, RoleVo>() {

    override fun map(from: RoleDo): RoleVo = mapping {}

}