package com.mao.service

import com.mao.entity.ErrorCode
import com.mao.ex.AppException
import com.mao.repository.RoleRepository
import com.mao.repository.UserRepository
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveUserDetailsServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) : ReactiveUserDetailsService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        log.info("spring security: found user ${user.username}")
        val roles = roleRepository.findRolesByUserId(user.id!!).mapNotNull { role -> role.name }.toList()
        log.info("spring security: found user ${user.username} with roles $roles")
        User.builder()
            .username(user.username ?: username)
            .password(user.password.orEmpty())
            .disabled(user.enabled != true)
            .accountExpired(user.expired == true)
            .accountLocked(user.locked == true)
            .credentialsExpired(false)
            .roles(*roles.toTypedArray())

            .build()
    }

}