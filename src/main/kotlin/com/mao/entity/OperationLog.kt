package com.mao.entity

import com.mao.extension.QueryField
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

enum class OperationScope { SYSTEM, DATA }

enum class OperationModule(val scope: OperationScope) {
    USER(OperationScope.SYSTEM), USER_PROFILE(OperationScope.SYSTEM), ROLE(OperationScope.SYSTEM),
    PERMISSION(OperationScope.SYSTEM), OPERATION_LOG(OperationScope.SYSTEM), DICT(OperationScope.SYSTEM),
    DEPARTMENT(OperationScope.SYSTEM), ANCIENT_BOOK(OperationScope.DATA), CHINESE_SURNAME(OperationScope.DATA),
    CRUDE_DRUG(OperationScope.DATA), LIVE(OperationScope.DATA), LOL(OperationScope.DATA),
    PICTURE(OperationScope.DATA), POEM(OperationScope.DATA), POET(OperationScope.DATA), UNSET(OperationScope.SYSTEM)
}

enum class Operation {
    ALL, PAGE, DETAIL, CREATE, UPDATE, DELETE,
    PASSWORD_UPDATE, PASSWORD_RESET, USER_RENEWAL, USER_LOCKED, USER_ENABLED,
    USER_ROLE, ROLE_PERMISSION, UNSET
}

@Table("sys_operation_log")
data class OperationLogDo(
    var id: Long? = null, var username: String? = null, var scope: String? = null,
    var module: String? = null, var operation: String? = null, var description: String? = null,
    var method: String? = null, var ip: String? = null, var success: Boolean? = null,
    var errorMessage: String? = null, var operationTime: LocalDateTime? = null, var cost: Long? = null
)

data class OperationLogQo(
    @QueryField var username: String? = null, @QueryField var module: String? = null,
    @QueryField var operation: String? = null,
    @QueryField(type = QueryField.Type.LIKE) var ip: String? = null,
    @QueryField var success: Boolean? = null
) : PageQo()

data class OperationLogVo(
    val id: Long? = null, val username: String? = null, val scope: String? = null,
    val module: String? = null, val operation: String? = null, val description: String? = null,
    val method: String? = null, val ip: String? = null, val success: Boolean? = null,
    val errorMessage: String? = null, val operationTime: LocalDateTime? = null, val cost: Long? = null
)
