package com.mao.entity

/**
 * 全局错误码
 */
enum class ErrorCode(val code: Int, val message: String) {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    ILLEGAL_PASSWORD(4001, "密码不合法"),
    ILLEGAL_EXPIRE_TIME(4002, "过期时间不合法"),
    DATA_NOT_FOUND(4003, "数据不存在"),

    // 认证异常
    AUTHENTICATION_ERROR(4101, "认证错误，请联系管理员"),
    AUTHENTICATION_TIMEOUT(4102, "认证超时"),
    USER_NOT_FOUND(4103, "用户或密码错误"),
    PASSWORD_ERROR(4104, "用户或密码错误"),
    USER_EXPIRED(4105, "账户已到期"),
    USER_LOCKED(4106, "账户已锁定"),
    USER_UNENABLED(4107, "账户已停用"),
    UNCHANGE_PASSWORD(4108, "请前往修改密码再进行登录"),
    MISS_TOKEN(4108, "认证信息缺失"),
    BAD_TOKEN(4109, "认证信息格式不正确"),
    INVALID_TOKEN(4110, "认证信息无效"),
    TOKEN_EXPIRED(4111, "认证已到期，请重新认证"),
    BAD_AUTHENTICATION_REQUEST(4112, "认证请求信息错误"),

    // 授权异常
    AUTHORIZATION_ERROR(4301, "授权错误，请联系管理员"),
    NO_PERMISSION(4302, "无此权限"),


    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    NOT_ACCEPTABLE(406, "无法提供该格式的数据"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的数据类型，请检查 Content-Type"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),
    DB_ERROR(500, "数据库操作异常"),
    INTERNAL_SERVER_ERROR(500, "服务器开小差了，请稍后再试"),
    BAD_GATEWAY(502, "网络或服务暂时不可用，请稍后重试"),

    ;

}

/**
 * 系统操作日志域
 */
enum class OperationScope {
    SYSTEM,     // 系统
    DATA,       // 数据
}

/**
 * 系统操作日志模块
 */
enum class OperationModule(val scope: OperationScope) {

    USER(OperationScope.SYSTEM),
    USER_PROFILE(OperationScope.SYSTEM),
    ROLE(OperationScope.SYSTEM),
    PERMISSION(OperationScope.SYSTEM),
    OPERATION_LOG(OperationScope.SYSTEM),
    DICT(OperationScope.SYSTEM),
    DEPARTMENT(OperationScope.SYSTEM),
    COMPANY(OperationScope.SYSTEM),

    ANCIENT_BOOK(OperationScope.DATA),
    CHINESE_SURNAME(OperationScope.DATA),
    CRUDE_DRUG(OperationScope.DATA),
    LIVE(OperationScope.DATA),
    LOL(OperationScope.DATA),
    PICTURE(OperationScope.DATA),
    POEM(OperationScope.DATA),
    POET(OperationScope.DATA),

    UNSET(OperationScope.SYSTEM),

}

/**
 * 系统操作日志-操作类型
 */
enum class Operation {

    ALL,                // 用于查询所有数据
    PAGE,               // 用于分页查询数据
    DETAIL,             // 用于查询数据详情
    CREATE,             // 用户新增数据
    UPDATE,             // 用于更新数据
    DELETE,             // 用于删除数据

    PASSWORD_UPDATE,    // 特殊 密码更新
    PASSWORD_RESET,     // 特殊 密码重置
    USER_RENEWAL,       // 特殊 用户续期
    USER_LOCKED,        // 特殊 用户解锁/锁定
    USER_ENABLED,       // 特殊 用户启用/停用
    USER_ROLE,          // 特殊 用户更新角色
    ROLE_PERMISSION,    // 特殊 角色更新权限

    UNSET,

}