package com.mao.user.mapper

import com.mao.user.entity.UserProfileDo
import com.mao.user.entity.UserProfileUpdateQo
import org.springframework.stereotype.Component

object UserProfileMapper {

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