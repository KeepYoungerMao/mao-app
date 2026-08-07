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

    private val allowedChars: Set<Char> = RandomUtils.RANDOM_CODE.map { it.toInt().and(0xFF).toChar() }.toSet()

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

    suspend fun generatePassword(): String = RandomUtils.pass(16)

    suspend fun isLegalPassword(password: String): Boolean = password.all { it in allowedChars }

}