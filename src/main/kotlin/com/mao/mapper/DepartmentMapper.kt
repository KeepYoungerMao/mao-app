package com.mao.mapper

import com.mao.entity.DepartmentDo
import com.mao.entity.DepartmentVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class DepartmentMapper {

    fun toVo(departmentDo: DepartmentDo): DepartmentVo = DepartmentViewMapper.map(departmentDo)
}

object DepartmentViewMapper : ObjectMappie<DepartmentDo, DepartmentVo>() {
    override fun map(from: DepartmentDo): DepartmentVo = mapping {}
}