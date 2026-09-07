package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeMaterialDo
import com.mao.employee.entity.EmployeeMaterialUpdateQo

object EmployeeMaterialMapper {

    fun copyToExistDo(
        request: EmployeeMaterialUpdateQo,
        material: EmployeeMaterialDo
    ): EmployeeMaterialDo = material.apply {
        request.materialName?.let { materialName = it }
        request.filePath?.let { filePath = it }
        request.uploadTime?.let { uploadTime = it }
        request.description?.let { description = it }
    }

}