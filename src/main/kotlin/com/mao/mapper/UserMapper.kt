package com.mao.mapper

import com.mao.entity.domain.UserDo
import com.mao.entity.query.UserAddQo
import com.mao.entity.view.UserVo
import com.mao.extension.asDateStr
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserMapper {

    fun toVo(userDO: UserDo): UserVo = UserVoMapper.map(userDO)

    fun toDo(userAdd: UserAddQo): UserDo = UserDoMapper.map(userAdd)

    fun toNewDo(userAdd: UserAddQo): UserDo = UserDoMapper.map(userAdd).apply { id = null }

    fun copyToExistDo(userAdd: UserAddQo, userDo: UserDo): UserDo = userDo.apply {
        userAdd.username?.let { username = it }
        userAdd.avatar?.let { avatar = it }
        userAdd.phone?.let { phone = it }
        userAdd.email?.let { email = it }
        userAdd.expired?.let { expired = it }
        userAdd.locked?.let { locked = it }
        userAdd.enabled?.let { enabled = it }
        userAdd.expireTime?.let { expireTime = it }
    }

}

object UserVoMapper: ObjectMappie<UserDo, UserVo>() {
    override fun map(from: UserDo): UserVo = mapping {
        to::lastLoginTime fromExpression { from.lastLoginTime.asDateStr }
    }
}

object UserDoMapper: ObjectMappie<UserAddQo, UserDo>() {
    override fun map(from: UserAddQo): UserDo = mapping {}
}