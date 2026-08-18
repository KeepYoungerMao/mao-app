package com.mao.util

import java.security.SecureRandom

object RandomUtils {

    private val secureRandom = SecureRandom()

    private const val SPECIAL_CHARS = ".@#$%^&*_?!~"
    private const val DIGITS = "0123456789"
    private const val UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz"
    private const val LETTERS = UPPERCASE_CHARS + LOWERCASE_CHARS
    private const val ALL_CHARS = SPECIAL_CHARS + DIGITS + LETTERS
    private const val BETTER_CHARS = "0123456789ABCDEFGHJKMNPQRSTVWXYZabcdefghjkmnpqrstvwxyz"

    fun numbers(num: Int): String = randomString(num, DIGITS)

    fun letters(num: Int): String = randomString(num, LETTERS)

    fun chars(num: Int): String = randomString(num, ALL_CHARS)

    fun betterChars(num: Int): String = randomString(num, BETTER_CHARS)

    /**
     * 从调用方提供的字符表中安全随机生成指定长度的字符串。
     * 字符表由具体业务决定，避免随机工具持有密码等领域规则。
     */
    fun randomString(length: Int, alphabet: CharSequence): String {
        require(length >= 0) { "length must not be negative" }
        require(alphabet.isNotEmpty()) { "alphabet must not be empty" }
        return buildString(length) {
            repeat(length) {
                append(alphabet[secureRandom.nextInt(alphabet.length)])
            }
        }
    }

    /** 使用 Fisher-Yates 算法原地打乱字符数组。 */
    fun shuffle(chars: CharArray): CharArray {
        for (index in chars.lastIndex downTo 1) {
            val randomIndex = secureRandom.nextInt(index + 1)
            val current = chars[index]
            chars[index] = chars[randomIndex]
            chars[randomIndex] = current
        }
        return chars
    }

    fun sequence(num: Int): String {
        require(num > 13) { "num must be larger than 13" }
        return "${System.currentTimeMillis()}${numbers(num - 13)}"
    }

}

fun main() {
    println(RandomUtils.numbers(18))
    println(RandomUtils.letters(18))
    println(RandomUtils.chars(18))
    println(RandomUtils.betterChars(18))
    println(RandomUtils.sequence(18))
}