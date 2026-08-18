package com.mao.extension

import com.mao.config.JwtConfig
import com.mao.entity.ErrorCode
import com.mao.ex.AppException
import com.mao.util.RandomUtils
import com.mao.util.RsaUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.abs

/**
 * 密码处理器
 * 提供密码解密（前后端密文传输）、密码生成、密码合规判定
 */
@Component
class PasswordHandler(
    private val jwtConfig: JwtConfig,
    private val jwtService: JwtService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    // 用户设置密码时允许完整英文字母；生成密码时则排除易混淆字符。
    private val allowedPasswordChars = ALL_ALLOWED_PASSWORD_CHARS.toSet()

    suspend fun decryptPassword(password: String?, timestamp: Long?): String {
        if (password == null) {
            throw AppException(ErrorCode.BAD_REQUEST)
        }
        val privateKey = jwtService.getPrivateKey()
        if (jwtConfig.replayAttackCheck) {
            if (timestamp == null) {
                throw AppException(ErrorCode.BAD_REQUEST)
            }
            // 防止重放攻击，校验时间戳，允许30秒窗口期
            val now = System.currentTimeMillis()
            if (abs(now - timestamp) > jwtConfig.replayAttackTime) {
                throw AppException(ErrorCode.AUTHENTICATION_TIMEOUT)
            }
            // 解密
            val rawPasswordWithTimestamp = try {
                RsaUtils.decrypt(password, privateKey)
            } catch (e: Exception) {
                log.error("Error decrypting password: ", e)
                throw AppException(ErrorCode.BAD_AUTHENTICATION_REQUEST)
            }
            // 检验密码格式是否正确
            if (!rawPasswordWithTimestamp.endsWith(":$timestamp")) {
                throw AppException(ErrorCode.BAD_REQUEST)
            }
            return rawPasswordWithTimestamp.removeSuffix(":$timestamp")
        } else {
            val password = try {
                RsaUtils.decrypt(password, privateKey)
            } catch (e: Exception) {
                log.error("Error decrypting password: ", e)
                throw AppException(ErrorCode.BAD_AUTHENTICATION_REQUEST)
            }
            // 兼容前端拼接时间戳
            return if (password.contains(":")) {
                password.split(":")[0]
            } else {
                password
            }
        }
    }

    suspend fun generatePassword(): String {
        // 先从四类字符中各取一个，保证生成结果必然满足最高复杂度要求。
        val password = buildString(GENERATED_PASSWORD_LENGTH) {
            append(RandomUtils.randomString(1, SPECIAL_CHARS))
            append(RandomUtils.randomString(1, DIGITS))
            append(RandomUtils.randomString(1, GENERATED_UPPERCASE_CHARS))
            append(RandomUtils.randomString(1, GENERATED_LOWERCASE_CHARS))
            // 剩余位置从完整生成字符集中随机补齐，避免各类字符数量固定。
            append(
                RandomUtils.randomString(
                    GENERATED_PASSWORD_LENGTH - PASSWORD_CATEGORY_COUNT,
                    ALL_GENERATED_PASSWORD_CHARS,
                )
            )
        }.toCharArray()
        // 打乱预先放入的四类字符，避免密码前四位形成可预测模式。
        return RandomUtils.shuffle(password).concatToString()
    }

    suspend fun isLegalPassword(password: String): Boolean {
        // 先拒绝长度越界和字符集之外的内容，再统计密码覆盖的字符类别。
        return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH &&
            password.all { it in allowedPasswordChars } &&
            listOf(
            password.any { it in SPECIAL_CHARS },
            password.any { it in DIGITS },
            password.any { it in ALLOWED_UPPERCASE_CHARS },
            password.any { it in ALLOWED_LOWERCASE_CHARS },
            ).count { it } >= MIN_PASSWORD_CATEGORY_COUNT
    }

    private companion object {
        const val GENERATED_PASSWORD_LENGTH = 16
        const val MIN_PASSWORD_LENGTH = 8
        const val MAX_PASSWORD_LENGTH = 64
        const val PASSWORD_CATEGORY_COUNT = 4
        const val MIN_PASSWORD_CATEGORY_COUNT = 3

        const val SPECIAL_CHARS = ".@#$%^&*_?!~"
        const val DIGITS = "0123456789"
        // 生成密码时排除 I、L、O、U 及对应小写字符，减少人工识别错误。
        const val GENERATED_UPPERCASE_CHARS = "ABCDEFGHJKMNPQRSTVWXYZ"
        const val GENERATED_LOWERCASE_CHARS = "abcdefghjkmnpqrstvwxyz"
        // 用户自行设置密码时不排除上述字母，避免无必要地限制合法输入。
        const val ALLOWED_UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val ALLOWED_LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz"
        const val ALL_GENERATED_PASSWORD_CHARS =
            SPECIAL_CHARS + DIGITS + GENERATED_UPPERCASE_CHARS + GENERATED_LOWERCASE_CHARS
        const val ALL_ALLOWED_PASSWORD_CHARS =
            SPECIAL_CHARS + DIGITS + ALLOWED_UPPERCASE_CHARS + ALLOWED_LOWERCASE_CHARS
    }

}