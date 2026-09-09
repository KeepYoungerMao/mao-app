package com.mao.department.mapper

import com.mao.department.entity.DepartmentAddQo
import com.mao.department.entity.DepartmentDo
import tech.mappie.api.ObjectMappie

object DepartmentCreateMapper : ObjectMappie<DepartmentAddQo, DepartmentDo>() {

    override fun map(from: DepartmentAddQo): DepartmentDo = mapping {}

}
