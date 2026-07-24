package com.mao.entity

enum class ErrorCode(val code: Int, val message: String) {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),

    // 认证异常
    AUTHENTICATION_ERROR(4010, "认证错误，请联系管理员"),
    USER_NOT_FOUND(4011, "用户或密码错误"),
    PASSWORD_ERROR(4012, "用户或密码错误"),
    USER_EXPIRED(4013, "账户已到期"),
    USER_LOCKED(4014, "账户已锁定"),
    USER_UNENABLED(4015, "账户已停用"),
    MISS_TOKEN(4016, "认证信息缺失"),
    BAD_TOKEN(4017, "认证信息格式不正确"),
    INVALID_TOKEN(4018, "认证信息无效"),
    TOKEN_EXPIRED(4019, "认证已到期，请重新认证"),

    // 授权异常
    AUTHORIZATION_ERROR(4031, "授权错误，请联系管理员"),
    NO_PERMISSION(4032, "无此权限"),


    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    DB_ERROR(500, "数据库操作异常"),
    INTERNAL_SERVER_ERROR(500, "系统内部异常"),

    DATA_NOT_FOUND(5001, "数据不存在")
    ;

}