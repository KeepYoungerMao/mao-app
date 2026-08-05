package com.mao.util

import java.nio.charset.StandardCharsets
import java.security.SecureRandom

object RandomUtils {

    // 在 Spring Boot 多线程并发下直接共享静态实例会导致状态破坏，此处使用 ThreadLocal 隔离
    private val SECURE_RANDOM = SecureRandom()

    /**
     * 随机字符表：
     * 0-12  : 特殊符号（共 12 个）
     * 12-22 : 数字（共 10 个）
     * 22-44 : 不包含 I, L, O, U 的大写字母（共 22 个）
     * 44-66 : 不包含 i, l, o, u 的小写字母（共 22 个）
     * 66-74 : 剩余字母（共 8 个）
     */
    private val RANDOM_CODE: ByteArray =
        ".@#$%^&*_?!~0123456789ABCDEFGHJKMNPQRSTVWXYZabcdefghjkmnpqrstvwxyziIlLoOuU"
            .toByteArray(StandardCharsets.UTF_8)

    fun numbers(num: Int): String = randomCode(num, 12, 22)

    fun letters(num: Int): String = randomCode(num, 22, 74)

    fun chars(num: Int): String = randomCode(num, 0, 74)

    fun betterChars(num: Int): String = randomCode(num, 12, 66)

    fun pass(num: Int): String = randomCode(num, 0, 66)

    fun sequence(num: Int): String {
        require(num > 13) { "num must be larger than 13" }
        return "${System.currentTimeMillis()}${numbers(num - 13)}"
    }

    private fun randomCode(num: Int, origin: Int, bound: Int): String {
        // 使用 Kotlin 原生的高效 ByteArray 构造器
        val bytes = ByteArray(num) {
            RANDOM_CODE[SECURE_RANDOM.nextInt(origin, bound)]
        }
        return String(bytes, Charsets.UTF_8)
    }

}

fun main() {
    println(RandomUtils.numbers(18))
    println(RandomUtils.letters(18))
    println(RandomUtils.chars(18))
    println(RandomUtils.betterChars(18))
    println(RandomUtils.pass(18))
    println(RandomUtils.sequence(18))
}