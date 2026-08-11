package com.mao.mapper

import com.mao.entity.UserAddQo
import com.mao.entity.UserProfileDo
import com.mao.entity.UserProfileUpdateQo
import com.mao.entity.UserProfileVo
import com.mao.util.DesensitizeUtils
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class UserProfileMapper {

    fun toVo(userProfileDo: UserProfileDo): UserProfileVo = UserProfileViewMapper.map(userProfileDo)

    fun toDo(userAddQo: UserAddQo) : UserProfileDo = UserProfileCreateMapper.map(userAddQo)

    fun copyToExistDo(userProfileUpdate: UserProfileUpdateQo, userProfileDo: UserProfileDo): UserProfileDo = userProfileDo.apply {
        userProfileUpdate.sexId?.let { sexId = it }
        userProfileUpdate.bloodTypeId?.let { bloodTypeId = it }
        userProfileUpdate.high?.let { high = it }
        userProfileUpdate.weight?.let { weight = it }
        userProfileUpdate.provinceId?.let { provinceId = it }
        userProfileUpdate.cityId?.let { cityId = it }
        userProfileUpdate.districtId?.let { districtId = it }
        userProfileUpdate.address?.let { address = it }
        userProfileUpdate.birthday?.let { birthday = it }
        userProfileUpdate.nationId?.let { nationId = it }
        userProfileUpdate.countryId?.let { countryId = it }
        userProfileUpdate.maritalId?.let { maritalId = it }
        userProfileUpdate.politicalId?.let { politicalId = it }
        userProfileUpdate.educationId?.let { educationId = it }
        userProfileUpdate.major?.let { major = it }
        userProfileUpdate.originProvinceId?.let { originProvinceId = it }
        userProfileUpdate.originCityId?.let { originCityId = it }
        userProfileUpdate.originDistrictId?.let { originDistrictId = it }
        userProfileUpdate.originAddress?.let { originAddress = it }
        userProfileUpdate.familyPhone?.let { familyPhone = it }
        userProfileUpdate.hobby?.let { hobby = it }
        userProfileUpdate.remark?.let { remark = it }
    }

}

object UserProfileViewMapper : ObjectMappie<UserProfileDo, UserProfileVo>() {
    override fun map(from: UserProfileDo): UserProfileVo = mapping {
        to::idCardNum fromExpression { DesensitizeUtils.desensitizeIdCardNumber(from.idCardNum) }
    }
}

object UserProfileCreateMapper : ObjectMappie<UserAddQo, UserProfileDo>() {
    override fun map(from: UserAddQo): UserProfileDo = mapping {}
}