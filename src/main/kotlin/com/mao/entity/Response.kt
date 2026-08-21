package com.mao.entity

import java.time.LocalDateTime

/**
 * 全局错误码枚举类
 */
enum class ErrorCode(val code: Int, val message: String) {
    SUCCESS(200, "成功"), 
    BAD_REQUEST(400, "请求参数错误"), 
    ILLEGAL_PASSWORD(4001, "密码不合法"),
    ILLEGAL_EXPIRE_TIME(4002, "过期时间不合法"), 
    DATA_NOT_FOUND(4003, "数据不存在"),
    
    AUTHENTICATION_ERROR(4101, "认证错误，请联系管理员"), 
    AUTHENTICATION_TIMEOUT(4102, "认证超时"),
    USER_NOT_FOUND(4103, "用户或密码错误"), 
    PASSWORD_ERROR(4104, "用户或密码错误"), 
    USER_EXPIRED(4105, "账户已到期"),
    USER_LOCKED(4106, "账户已锁定"), 
    USER_DISABLED(4107, "账户已停用"),
    UNCHANGE_PASSWORD(4108, "首次注册请前往修改密码再进行登录"), 
    PASSWORD_EDIT(4109, "密码已更改，请重新登录"),
    PASSWORD_RESET(4110, "密码已重置，请重新登陆"), 
    MISS_TOKEN(4111, "认证信息缺失"),
    BAD_TOKEN(4112, "认证信息格式不正确"), 
    INVALID_TOKEN(4113, "认证信息无效"),
    TOKEN_EXPIRED(4114, "认证已到期，请重新认证"), 
    BAD_AUTHENTICATION_REQUEST(4115, "认证请求信息错误"),
    
    AUTHORIZATION_ERROR(4301, "授权错误，请联系管理员"), 
    NO_PERMISSION(4302, "无此权限"),
    
    NOT_FOUND(404, "资源不存在"), 
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    NOT_ACCEPTABLE(406, "无法提供该格式的数据"), 
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的数据类型，请检查 Content-Type"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"), 
    DB_ERROR(500, "数据库操作异常"),
    INTERNAL_SERVER_ERROR(500, "服务器开小差了，请稍后再试"), 
    BAD_GATEWAY(502, "网络或服务暂时不可用，请稍后重试")
}

/**
 * 全局接口响应统一包装类
 */
data class Response<T>(val code: Int, val message: String = "success", val data: T? = null) {

    companion object {

        fun <T> success(data: T): Response<T> {
            return Response(code = ErrorCode.SUCCESS.code, data = data)
        }

        fun error(code: Int, message: String): Response<Nothing?> {
            return Response(code = code, message = message)
        }

        fun error(errorCode: ErrorCode): Response<Nothing?> {
            return Response(code = errorCode.code, message = errorCode.message)
        }

    }

}

/**
 * 分页数据包装类
 */
data class PageResponse<T>(val pageNum: Int, val pageSize: Int, val total: Long, val records: List<T>) {

    /**
     * 提供一个支持流式转换的 map 工具方法
     */
    fun <R> map(mapper: (T) -> R): PageResponse<R> {
        val mappedList = this.records.map(mapper)
        return PageResponse(this.pageNum, this.pageSize, this.total, mappedList)
    }

}

/**
 * 提示信息包装类
 * 用于操作类接口无实际返回数据时，返回操作结果提示信息
 */
data class Tips(val message: String, val operationTime: LocalDateTime = LocalDateTime.now())