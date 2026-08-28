package com.mao.user.mapper

import com.mao.user.entity.UserDo
import com.mao.user.entity.UserUpdateQo

object UserMapper {

    // 增量更新写法
     fun copyToExistDo(userUpdate: UserUpdateQo, userDo: UserDo): UserDo = userDo.apply {
        userUpdate.username?.let { username = it }
        userUpdate.avatar?.let { avatar = it }
        userUpdate.phone?.let { phone = it }
        userUpdate.email?.let { email = it }
     }

}