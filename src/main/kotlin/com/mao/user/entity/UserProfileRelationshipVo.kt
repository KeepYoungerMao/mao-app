package com.mao.user.entity

import com.mao.common.entity.BaseVo

/**
 * 用户人员关系查询结果包装类
 */
data class UserProfileRelationshipVo(
    val id: Int? = null,
    val userId: Int? = null,
    val realName: String? = null,
    val relationshipId: Int? = null,
    val idCardNum: String? = null,
    val phone: String? = null,
    val remark: String? = null
) : BaseVo()
