package com.mao.extension

import com.mao.entity.ErrorCode
import com.mao.entity.Response
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.security.authentication.*
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import tools.jackson.databind.json.JsonMapper

/**
 * # 认证/授权失败处理器
 * ## 认证异常子类：
 * | 异常类                                        | 解释                  | 大类 |
 * | :------------------------------------------  | :------------------- | :-- |
 * | AccountExpiredException                      | 用户账号到期           | 1   |
 * | AccountStatusException                       | 基类                 |     |
 * | AuthenticationCredentialsNotFoundException   | 没携带token           | 2   |
 * | AuthenticationServiceException               | 认证服务器内部         | 2   |
 * | BadCredentialsException                      | 无效凭证              | 2   |
 * | CompromisedPasswordException                 | 公开的密码             |     |
 * | CookieTheftException                         | cookie被盗用          |     |
 * | CredentialsExpiredException                  | 凭证过期              | 2   |
 * | DisabledException                            | 账号停用              | 1   |
 * | InsufficientAuthenticationException          | 匿名访问需要登陆的接口   | 2   |
 * | InternalAuthenticationServiceException       | 获取用户信息时服务内部   | 1   |
 * | InvalidBearerTokenException                  | token非法             | 2   |
 * | InvalidCookieException                       | cookie非法            |     |
 * | InvalidOneTimeTokenException                 | 一次性登录令牌无效       |     |
 * | LockedException                              | 账号锁定               | 1   |
 * | NonceExpiredException                        | Digest-Nonce随机串过期 |     |
 * | OAuth2AuthenticationException                | OAuth2认证失败         |     |
 * | PreAuthenticatedCredentialsNotFoundException | 预认证失败             |     |
 * | ProviderNotFoundException                    | 未找到身份认证处理器     |     |
 * | RememberMeAuthenticationException            | 记住我认证异常          |     |
 * | SessionAuthenticationException               | session拦截策略认证拒绝  |     |
 * | UsernameNotFoundException                    | 用户不存在              | 1   |
 *
 * 1. 登录获取token时可能会发生：
 *     - 用户不存在: UsernameNotFoundException
 *     - 获取用户内部错误: InternalAuthenticationServiceException
 *     - 用户到期: AccountExpiredException
 *     - 用户锁定: LockedException
 *     - 用户停用: DisabledException
 * 2. 访问接口时token认证可能会发生：
 *     - 服务器内部错误: AuthenticationServiceException
 *     - 没携带token: AuthenticationCredentialsNotFoundException
 *     - 匿名访问需要登陆的接口: InsufficientAuthenticationException
 *     - token格式不对（无效）: BadCredentialsException
 *     - 凭证过期: CredentialsExpiredException
 *     - token非法: InvalidBearerTokenException
 *
 * ## 授权异常子类
 * | 异常类                                        | 解释                  | 大类 |
 * | :------------------------------------------  | :------------------- | :-- |
 * | AuthorizationDeniedException                 | 授权异常              | 1   |
 * | AuthorizationServiceException                | 授权内部服务异常        | 1   |
 * | CsrfException                                | 跨站请求伪造           |     |
 * | InvalidCsrfTokenException                    | 跨站请求伪造           |     |
 * | MissingCsrfTokenException                    | 跨站请求伪造           |     |
 *
 * 1. 权限验证时可能会发生：
 *     - 没有权限: AuthorizationDeniedException
 *     - 鉴权时内部服务错误: AuthorizationServiceException
 */
@Component
class CustomAuthHandler(
    private val jsonMapper: JsonMapper
) : ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 认证错误处理
     */
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        log.error("authentication exception: {}", ex.javaClass.name)
        val errorCode = when (ex) {
            is DisabledException -> ErrorCode.USER_DISABLED
            is AccountExpiredException -> ErrorCode.USER_EXPIRED
            is LockedException -> ErrorCode.USER_LOCKED
            is AuthenticationServiceException -> ErrorCode.AUTHENTICATION_ERROR
            is AuthenticationCredentialsNotFoundException -> ErrorCode.MISS_TOKEN
            is InsufficientAuthenticationException -> ErrorCode.MISS_TOKEN
            is BadCredentialsException -> ErrorCode.BAD_TOKEN
            is CredentialsExpiredException -> ErrorCode.TOKEN_EXPIRED
            is InvalidBearerTokenException -> ErrorCode.INVALID_TOKEN
            else -> ErrorCode.AUTHENTICATION_ERROR
        }
        return writeError(exchange, errorCode)
    }

    /**
     * 授权错误处理
     */
    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        log.error("authorize exception: {}", denied.javaClass.name)
        val errorCode = when (denied) {
            is AuthorizationServiceException -> ErrorCode.AUTHORIZATION_ERROR
            is AuthorizationDeniedException -> ErrorCode.NO_PERMISSION
            else -> ErrorCode.AUTHORIZATION_ERROR
        }
        return writeError(exchange, errorCode)
    }

    private fun writeError(exchange: ServerWebExchange, errorCode: ErrorCode): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.OK
        response.headers.contentType = MediaType.APPLICATION_JSON
        val responseData = Response.error(errorCode)
        val bytes = jsonMapper.writeValueAsBytes(responseData)
        val buffer = response.bufferFactory().wrap(bytes)
        return response.writeWith(Mono.just(buffer))
    }

}