package com.mao.entity

import com.mao.extension.QueryField
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

data class RoleQo(
    @QueryField(type = QueryField.Type.LIKE) 
    val name: String? = null
) : PageQo()

@Table("sys_role")
data class RoleDo(
    @Id 
    var id: Int? = null, 
    var name: String? = null, 
    var description: String? = null
) : BaseDo()

@Table("sys_role_permission_ref")
data class RolePermissionRefDo(
    @Id 
    val id: Int, 
    val roleId: Int, 
    val permissionId: Int
)

/**
 * 角色信息实体类
 */
data class RoleVo(
    val id: Int? = null, 
    val name: String? = null, 
    val description: String? = null
) : BaseVo()

data class RolePermissionUpdateQo(
    @field:NotNull 
    val id: Int? = null, 
    val permissionIds: List<Int>? = null
)
