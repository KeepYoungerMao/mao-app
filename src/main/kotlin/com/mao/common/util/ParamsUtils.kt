package com.mao.common.util

import com.mao.common.entity.ErrorCode
import com.mao.common.ex.AppException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * ## 参数NULL检验
 * 对任意数据进行null检查，如果为null，抛出BAD_REQUEST异常
 *
 * ### inline 内联函数
 * 1. 该函数接收一个`message: () -> String`函数类型参数，每次调用都会产生一个`Function`对象。
 *    加上inline后，编译器会把该函数代码直接拷贝到调用处，避免函数类型参数带来的对象分配。
 * 2. contract 契约功能需要inline关键字修饰。
 *
 * ### 为什么使用函数类型参数
 * 使用`message: () -> String`是为了性能优化，惰性求值。
 * 用户传递的message可能是：`"用户[${user.name}]登录缺少鉴权信息"`，
 * 如果参数类型直接是`String`，在调用这个函数之前，就需要把这个参数给拼接好，因此会创建`StringBuilder`额外对象。
 * 如果使用函数类型参数，如果value不为null，拼接动作将不会执行。只有value为null时，内部动作才开始。
 *
 * ### contract 契约
 * 它可以告诉kotlin编译器，如果该函数正常返回value，没有抛出异常，那value一定不为null。
 * 这使得你在调用后，编译器会自动将value智能转换为非空类型，无需手动写`!!`。
 *
 * **注意**：contract是实验性功能，因此需要加上`@OptIn(ExperimentalContracts::class)`这一注解
 *
 * @see requireNotNull 仿照该函数编写
 * @param value 要判断的值
 * @param message 错误信息
 * @return value
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T: Any> notNull(value: T?, message: () -> String): T {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        throw AppException(ErrorCode.BAD_REQUEST, message())
    } else {
        return value
    }
}