package com.mao

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
class AppTests {

    @Test
    fun contextLoads() {
        val passwordEncoder = BCryptPasswordEncoder()
        val password = passwordEncoder.encode("test")
        println("password: $password")
    }

}
