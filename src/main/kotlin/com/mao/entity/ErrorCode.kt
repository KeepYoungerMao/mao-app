package com.mao.entity

enum class ErrorCode(val code: Int, val message: String) {

    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    DB_ERROR(500, "数据库操作异常"),
    INTERNAL_SERVER_ERROR(500, "系统内部异常"),

    DATA_NOT_FOUND(5001, "数据不存在")
    ;

}