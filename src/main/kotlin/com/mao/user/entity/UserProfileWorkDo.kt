package com.mao.user.entity

import com.mao.common.entity.BaseDo
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

/**
 * 用户工作经历实体类
 */
@Table("sys_user_profile_work")
data class UserProfileWorkDo(
    @Id
    var id: Int? = null,
    var userId: Int? = null,
    var companyName: String? = null,
    var jobTitle: String? = null,
    var industry: String? = null,
    var industryId: Int? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var responsibilities: String? = null,
    var currentEmployment: Boolean? = null
) : BaseDo()
