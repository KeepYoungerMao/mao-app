package com.mao.extension

import com.mao.entity.ErrorCode
import com.mao.entity.Response
import com.mao.ex.AppException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

/**
 * 全局异常处理器
 * 此处不能使用 suspend 挂起函数，即协程代替 Mono/Flux
 * 其他向 controller，service，repository 可以使用 suspend 代替，
 * 由kotlinx-coroutines-reactor库起到关键作用，作为与webflux连接的桥梁。
 * 1. 因为请求整个处理链处于协程上下文中，全局异常处理器处于webflux的最外层，当业务代码出现异常抛出，
 * 会被webflux的Dispatcher捕获，并寻找合适的@ExceptionHandler处理，如果异常处理器的方法是 suspend的，
 * Spring必须在异常发生后额外开启或恢复一个协程来处理，降低性能，因为异常处理器只需做简单的异常返回，无需需要挂起的异步操作。
 * 2. 像请求参数错误，http method错误等都没有进入controller
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 捕获自定义业务异常
     */
    @ExceptionHandler(AppException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleBusinessException(e: AppException): Mono<Response<Nothing?>> {
        log.warn("业务异常: {}", e.message)
        return Mono.just(Response.error( e.code.code, e.message ?: "业务异常"))
    }

    /**
     * 捕获参数校验异常 (对应 @Valid 校验失败)
     * WebFlux 中为 WebExchangeBindException (而非 Spring MVC 的 MethodArgumentNotValidException)
     */
    @ExceptionHandler(WebExchangeBindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleWebExchangeBindException(e: WebExchangeBindException): Mono<Response<Nothing?>> {
        // 直接使用 Kotlin 的 joinToString，一行搞定 map + join，无需 Stream 流
        val errMsg = e.bindingResult.fieldErrors
            .joinToString(separator = "; ") { it.defaultMessage ?: "参数错误" }

        log.warn("参数校验异常: {}", errMsg)
        return Mono.just(Response.error(ErrorCode.BAD_REQUEST.code, errMsg))
    }

    /**
     * 捕获参数映射/解析异常 (例如 JSON 解析失败、Query 参数缺失等)
     * WebFlux 中为 ServerWebInputException (而非 MVC 中的 HttpMessageNotReadableException)
     */
    @ExceptionHandler(ServerWebInputException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleServerWebInputException(e: ServerWebInputException): Mono<Response<Nothing?>> {
        log.warn("输入参数解析异常: {}", e.reason)
        return Mono.just(Response.error(ErrorCode.BAD_REQUEST.code, "参数解析失败: ${e.reason}"))
    }

    /**
     * 捕获请求 Method 不匹配异常
     */
    @ExceptionHandler(MethodNotAllowedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handleMethodNotAllowedException(e: MethodNotAllowedException): Mono<Response<Nothing?>> {
        log.warn("不支持的请求方法: {}", e.message)
        return Mono.just(Response.error(ErrorCode.METHOD_NOT_ALLOWED.code, "不支持的方法: ${e.httpMethod}"))
    }

    /**
     * 捕获通用的 HTTP 状态异常 (如 WebFlux 自带的 404 等)
     */
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): Mono<Response<Nothing?>> {
        val httpValue = e.statusCode.value()
        log.warn("HTTP状态异常 [{}]: {}", httpValue, e.reason ?: e.message)
        val friendlyMessage = when (httpValue) {
            400 -> "请求参数错误，请检查输入格式"
            401 -> "未授权或登录已过期，请重新登录"
            403 -> "抱歉，您没有权限访问该资源"
            404 -> "您请求的接口或资源不存在"
            405 -> "请求方式不支持 (例如请检查是否将 GET 错写成 POST)"
            406 -> "无法提供该格式的数据"
            415 -> "不支持的数据类型，请检查 Content-Type"
            429 -> "请求过于频繁，请稍后重试"
            500 -> "服务器开小差了，请稍后再试"
            502, 503, 504 -> "网络或服务暂时不可用，请稍后重试"
            else -> e.reason ?: "系统繁忙，请稍后重试" // 兜底策略
        }
        return Mono.just(Response.error(httpValue, friendlyMessage))
    }

    /**
     * 捕获 R2DBC / 数据库操作异常
     * Spring Data 会将底层异常包装为 DataAccessException 体系
     */
    @ExceptionHandler(DataAccessException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleDataAccessException(e: DataAccessException): Mono<Response<Nothing?>> {
        log.error("数据库操作发生异常: ", e)
        return Mono.just(Response.error(ErrorCode.DB_ERROR))
    }

    /**
     * 顶级兜底未知异常
     */
    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleThrowable(e: Throwable): Mono<Response<Nothing?>> {
        log.error("系统发生未捕获异常: ", e)
        return Mono.just(Response.error(ErrorCode.INTERNAL_SERVER_ERROR))
    }

}