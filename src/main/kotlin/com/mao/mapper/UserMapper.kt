package com.mao.mapper

import com.mao.entity.domain.UserDo
import com.mao.entity.query.UserAddQo
import com.mao.entity.query.UserUpdateQo
import com.mao.entity.view.UserVo
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserMapper {

    fun toVo(userDO: UserDo): UserVo = UserViewMapper.map(userDO)

    fun toDo(userAdd: UserAddQo): UserDo = UserCreateMapper.map(userAdd)

    // 增量更新写法
     fun copyToExistDo(userUpdate: UserUpdateQo, userDo: UserDo): UserDo = userDo.apply {
        userUpdate.username?.let { username = it }
        userUpdate.avatar?.let { avatar = it }
        userUpdate.phone?.let { phone = it }
        userUpdate.email?.let { email = it }
        userUpdate.expireTime?.let { expireTime = it }
     }

}

object UserViewMapper: ObjectMappie<UserDo, UserVo>() {
    override fun map(from: UserDo): UserVo = mapping {}
}

object UserCreateMapper: ObjectMappie<UserAddQo, UserDo>() {
    override fun map(from: UserAddQo): UserDo = mapping {}
}