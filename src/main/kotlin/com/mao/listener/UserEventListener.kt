package com.mao.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class UserEventListener(
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}") val fromEmail: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handleUserCreateEvent(event: UserCreateEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val message = SimpleMailMessage().apply {
                    from = fromEmail
                    setTo(event.email)
                    subject = "用户创建成功通知"
                    text = "尊敬的 ${event.name} 先生/女士：\n感谢您使用 MAO-APP，您已成功创建用户，初始密码为：\n${event.password}\n为了您的账号安全，请在首次登陆后尽快更改密码。"
                }
                mailSender.send(message)
            }.onFailure { e ->
                log.error("Exception occurred while handling user creation: ", e)
            }
        }
    }

    @EventListener
    fun handleUserPasswordResetEvent(event: UserPasswordResetEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val message = SimpleMailMessage().apply {
                    from = fromEmail
                    setTo(event.email)
                    subject = "密码重置成功通知"
                    text = "你在 MAO-APP 中的密码已重置，重置后密码为：\n${event.password}\n请妥善保管。"
                }
                mailSender.send(message)
            }.onFailure { e ->
                log.error("Exception occurred while handling user password reset: ", e)
            }
        }
    }

}