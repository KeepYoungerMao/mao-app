package com.mao.user.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * 用户上传材料实体类
 */
@Table("sys_user_profile_material")
data class UserProfileMaterialDo(
    @Id
    var id: Int? = null,
    var userId: Int? = null,
    var materialName: String? = null,
    var filePath: String? = null,
    var uploadTime: LocalDateTime? = null,
    var description: String? = null
) : BaseDo()
