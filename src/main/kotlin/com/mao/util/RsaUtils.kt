package com.mao.util

import com.mao.config.RSA_OAEP_SPEC
import com.mao.config.RSA_TRANSFORMATION
import org.springframework.core.io.Resource
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

object RsaUtils {

    fun parsePublicKey(resource: Resource): RSAPublicKey {
        val content = resource.inputStream.bufferedReader().use { it.readText() }
        val cleanKey = content
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s+".toRegex(), "") // 移除换行与空格

        val decoded = Base64.getDecoder().decode(cleanKey)
        val spec = X509EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
    }

    fun parsePrivateKey(resource: Resource): RSAPrivateKey {
        val content = resource.inputStream.bufferedReader().use { it.readText() }
        val cleanKey = content
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s+".toRegex(), "")

        val decoded = Base64.getDecoder().decode(cleanKey)
        val spec = PKCS8EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePrivate(spec) as RSAPrivateKey
    }

    fun encrypt(content: String, publicKey: RSAPublicKey): String {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, RSA_OAEP_SPEC)
        val encryptedBytes = cipher.doFinal(content.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(content: String, privateKey: RSAPrivateKey): String {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey, RSA_OAEP_SPEC)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(content))
        return String(decryptedBytes, Charsets.UTF_8)
    }

    fun generatorRsa() {
        // 初始化 2048 位的 RSA 秘钥生成器
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }
        val keyPair = keyPairGenerator.generateKeyPair()
        // 使用 Base64 MIME 编码器（每 64 个字符自动换行，符合标准 PEM 格式）
        val encoder = Base64.getMimeEncoder(64, "\n".toByteArray())
        // 构建 PKCS#8 私钥文件内容
        val privateKeyPem = """
            |-----BEGIN PRIVATE KEY-----
            |${encoder.encodeToString(keyPair.private.encoded)}
            |-----END PRIVATE KEY-----
        """.trimMargin()
        // 构建 X.509 公钥文件内容
        val publicKeyPem = """
            |-----BEGIN PUBLIC KEY-----
            |${encoder.encodeToString(keyPair.public.encoded)}
            |-----END PUBLIC KEY-----
        """.trimMargin()
        print(privateKeyPem)
        print("\n")
        print(publicKeyPem)
    }

}

fun main() {
    RsaUtils.generatorRsa()
}