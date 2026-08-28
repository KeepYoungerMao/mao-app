package com.mao.user.entity

import com.mao.common.entity.BaseVo
import java.time.LocalDateTime

/**
 * 用户上传材料查询结果包装类
 */
data class UserProfileMaterialVo(
    val id: Int? = null,
    val userId: Int? = null,
    val materialName: String? = null,
    val filePath: String? = null,
    val uploadTime: LocalDateTime? = null,
    val description: String? = null
) : BaseVo()
