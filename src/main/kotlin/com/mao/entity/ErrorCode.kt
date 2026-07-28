package com.mao.entity

enum class ErrorCode(val code: Int, val message: String) {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),

    // 认证异常
    AUTHENTICATION_ERROR(4101, "认证错误，请联系管理员"),
    AUTHENTICATION_TIMEOUT(4102, "认证超时"),
    USER_NOT_FOUND(4103, "用户或密码错误"),
    PASSWORD_ERROR(4104, "用户或密码错误"),
    USER_EXPIRED(4105, "账户已到期"),
    USER_LOCKED(4106, "账户已锁定"),
    USER_UNENABLED(4107, "账户已停用"),
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

    DATA_NOT_FOUND(5001, "数据不存在")
    ;

}