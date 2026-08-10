package com.mao.service

import com.mao.entity.AuthUserDetails
import com.mao.entity.ErrorCode
import com.mao.ex.AppException
import com.mao.repository.UserRepository
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveUserDetailsServiceImpl(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        log.info("spring security: found user ${user.username}")
        AuthUserDetails.create(user)
    }

}