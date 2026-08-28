package com.mao.department.mapper

import com.mao.department.entity.DepartmentDo
import com.mao.department.entity.DepartmentVo
import tech.mappie.api.ObjectMappie

object DepartmentViewMapper : ObjectMappie<DepartmentDo, DepartmentVo>() {

    override fun map(from: DepartmentDo): DepartmentVo = mapping {}

}