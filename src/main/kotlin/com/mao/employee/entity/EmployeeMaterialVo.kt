package com.mao.employee.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDateTime

/**
 * 用户上传材料查询结果包装类
 */
data class EmployeeMaterialVo(
    val id: Int? = null,
    val employeeId: Int? = null,
    val materialName: String? = null,
    val filePath: String? = null,
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
) : BaseVo()
