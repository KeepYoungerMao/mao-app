package com.mao.user.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * 用户人员关系实体类
 */
@Table("sys_user_profile_relationship")
data class UserProfileRelationshipDo(
    @Id
    var id: Int? = null,
    var userId: Int? = null,
    var realName: String? = null,
    var relationshipId: Int? = null,
    var idCardNum: String? = null,
    var phone: String? = null,
    var remark: String? = null
) : BaseDo()
