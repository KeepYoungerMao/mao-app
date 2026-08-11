package com.mao

import com.mao.entity.UserAddQo
import com.mao.mapper.UserCreateMapper
import com.mao.mapper.UserProfileCreateMapper
import com.mao.util.RsaUtils
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.interfaces.RSAPublicKey
import java.time.LocalDate
import java.time.LocalDateTime

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

    @Test
    fun testMappie() {
        val userAdd = UserAddQo(
            username = "test_username",
            avatar = "test_avatar",
            phone = "test_phone",
            email = "test_email",
            expireTime = LocalDateTime.now().plusDays(1),
            realName = "test_name",
            entryDate = LocalDate.now(),
            idCardNum = "124124618947191391",
            birthday = LocalDate.of(1970, 1, 1),
        )
        val user = UserCreateMapper.map(userAdd)
        println(user)
        val userProfile = UserProfileCreateMapper.map(userAdd)
        println(userProfile)
    }

}
