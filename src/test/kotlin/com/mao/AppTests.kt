package com.mao

import com.mao.util.RsaUtils
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.interfaces.RSAPublicKey

@SpringBootTest
class AppTests {

    @Test
    fun contextLoads() {
        val passwordEncoder = BCryptPasswordEncoder()
        val password = passwordEncoder.encode("test")
        println("password: $password")
    }

    @Test
    fun rsaEncryptPassword() {
        val publicContent = this::class.java.classLoader
            .getResourceAsStream("public-key.pem")!!
            .bufferedReader()
            .readText()
        val publicKey: RSAPublicKey = RsaUtils.parsePublicKey(publicContent)
        val timestamp = System.currentTimeMillis()
        println(timestamp)
        val encode = RsaUtils.encrypt("test:${timestamp}", publicKey)
        println(encode)
    }

}
