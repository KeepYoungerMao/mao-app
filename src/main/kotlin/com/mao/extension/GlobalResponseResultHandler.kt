package com.mao.extension

import com.mao.entity.Response
import org.reactivestreams.Publisher
import org.springframework.core.MethodParameter
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.reactive.result.view.Rendering
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 全局响应结果包装处理器
 */
class GlobalResponseResultHandler(
    writers: List<HttpMessageWriter<*>>,
    resolver: RequestedContentTypeResolver,
    private val adapterRegistry: ReactiveAdapterRegistry
) : ResponseBodyResultHandler(writers, resolver, adapterRegistry) {

    init {
        // 设置优先级高于默认的 ResponseBodyResultHandler (默认值为 100)
        // 这样可以确保我们的处理器优先拦截到请求
        order = 99
    }

    /**
     * 判断当前请求是否需要被包装
     */
    override fun supports(result: HandlerResult): Boolean {
        val returnTypeSource = result.returnTypeSource
        // 只有标注了 @RestController 或 @ResponseBody 的方法才处理
        val isRest = returnTypeSource.hasMethodAnnotation(ResponseBody::class.java) ||
                returnTypeSource.declaringClass.isAnnotationPresent(RestController::class.java) ||
                returnTypeSource.declaringClass.isAnnotationPresent(ResponseBody::class.java)

        if (!isRest) return false

        val returnType = result.returnType
        var actualClass = returnType.toClass()

        // 动态提取响应式流 (Mono, Flux, Flow) 或协程内部的真实泛型类型
        // 避免诸如 Mono<Resource> 被误判为 Mono 的情况
        val adapter = adapterRegistry.getAdapter(actualClass)
        if (adapter != null) {
            actualClass = returnType.resolveGeneric(0) ?: Any::class.java
        }

        // 排除不需要包装的特殊类型
        return when {
            // ResponseEntity 本身带有状态码和 Header，交由底层自行处理
            ResponseEntity::class.java.isAssignableFrom(actualClass) -> false
            // 静态资源、文件下载流
            Resource::class.java.isAssignableFrom(actualClass) || actualClass == ByteArray::class.java -> false
            // 页面跳转/模板渲染 (Thymeleaf/Freemarker)
            Rendering::class.java.isAssignableFrom(actualClass) -> false
            // SSE 流式接口 (Server-Sent Events)
            ServerSentEvent::class.java.isAssignableFrom(actualClass) -> false
            else -> true
        }
    }

    /**
     * 执行包装逻辑
     */
    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        val body = result.returnValue
        val returnType = result.returnType

        // 获取该类型的响应式适配器（能统一处理 Mono, Flux, 协程 Flow 和 suspend 返回值）
        val adapter = adapterRegistry.getAdapter(returnType.resolve(), body)

        val wrappedPublisher: Publisher<*> = if (adapter != null) {
            val publisher = adapter.toPublisher<Any>(body)

            if (adapter.isMultiValue) {
                // 如果是 Flux 或 Flow，将其收集为 List 后再包装
                Flux.from(publisher)
                    .collectList()
                    .map { wrap(it) }
                    .defaultIfEmpty(wrap(emptyList<Any>()))
            } else {
                // 如果是 Mono 或 suspend fun 返回单值
                Mono.from(publisher)
                    .map { wrap(it) }
                    .defaultIfEmpty(wrap(null)) // 兼容 suspend fun 返回 Unit 的情况
            }
        } else {
            // 普通同步对象直接包装
            Mono.just(wrap(body))
        }

        // 创建新的 HandlerResult，替换掉原始的 body
        val wrappedResult = HandlerResult(
            result.handler,
            wrappedPublisher,
            WRAPPER_METHOD_PARAM
        )

        // 交给父类完成标准的 Content Negotiation 和 JSON 序列化
        return super.handleResult(exchange, wrappedResult)
    }

    /**
     * 内部包装方法，防止被重复包装（例如全局异常处理器已经返回了 Response 对象）
     */
    private fun wrap(data: Any?): Response<Any?> {
        if (data is Response<*>) {
            @Suppress("UNCHECKED_CAST")
            return data as Response<Any?>
        }
        return Response.success(data)
    }

    companion object {
        // 利用虚拟方法获取 MethodParameter。
        // 这一步非常关键：Jackson 序列化时需要正确的泛型上下文，如果不提供，泛型信息会擦除导致部分嵌套 JSON 序列化异常。
        @JvmStatic
        @Suppress("unused")
        fun globalResponseDummyMethod(): Mono<Response<Any>> = Mono.empty()

        private val WRAPPER_METHOD_PARAM: MethodParameter by lazy {
            val method = GlobalResponseResultHandler::class.java.getDeclaredMethod("globalResponseDummyMethod")
            MethodParameter(method, -1)
        }
    }
}