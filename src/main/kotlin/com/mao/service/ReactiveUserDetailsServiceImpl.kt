package com.mao.service

import com.mao.entity.AuthUserDetails
import com.mao.entity.ErrorCode
import com.mao.ex.AppException
import com.mao.repository.RolePermissionRefRepository
import com.mao.repository.UserRepository
import com.mao.repository.UserRoleRefRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveUserDetailsServiceImpl(
    private val userRepository: UserRepository,
    private val userRoleRefRepository: UserRoleRefRepository,
    private val rolePermissionRefRepository: RolePermissionRefRepository
) : ReactiveUserDetailsService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user = userRepository.findByUsername(username) ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        log.info("spring security: found user ${user.username}")
        // 查询用户拥有的角色
        val authorities = buildList {
            val userRoles = userRoleRefRepository.getRoleByUsername(username)
                .filterNotNull()
                .filter { it.id != null && it.name != null }.toList()
            if (userRoles.isNotEmpty()) {
                addAll(userRoles.map { SimpleGrantedAuthority("ROLE_${it.name}") })
                val roleIds = userRoles.mapNotNull { it.id }
                val permissions = rolePermissionRefRepository.getPermissionByRoleIdIn(roleIds)
                    .filterNotNull()
                    .filter { it.isNotBlank() }
                    .toList()
                addAll(permissions.map { SimpleGrantedAuthority(it) })
            }
        }
        AuthUserDetails.create(user, authorities)
    }

}