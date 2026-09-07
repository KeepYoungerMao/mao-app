package com.mao.employee.mapper

import com.mao.employee.entity.EmployeeDo
import com.mao.employee.entity.EmployeeUpdateQo

object EmployeeMapper {

    fun copyToExistDo(employeeUpdate: EmployeeUpdateQo, employeeDo: EmployeeDo): EmployeeDo = employeeDo.apply {
        employeeUpdate.sexId?.let { sexId = it }
        employeeUpdate.bloodTypeId?.let { bloodTypeId = it }
        employeeUpdate.high?.let { high = it }
        employeeUpdate.weight?.let { weight = it }
        employeeUpdate.provinceId?.let { provinceId = it }
        employeeUpdate.cityId?.let { cityId = it }
        employeeUpdate.districtId?.let { districtId = it }
        employeeUpdate.address?.let { address = it }
        employeeUpdate.birthday?.let { birthday = it }
        employeeUpdate.nationId?.let { nationId = it }
        employeeUpdate.countryId?.let { countryId = it }
        employeeUpdate.maritalId?.let { maritalId = it }
        employeeUpdate.politicalId?.let { politicalId = it }
        employeeUpdate.educationId?.let { educationId = it }
        employeeUpdate.major?.let { major = it }
        employeeUpdate.originProvinceId?.let { originProvinceId = it }
        employeeUpdate.originCityId?.let { originCityId = it }
        employeeUpdate.originDistrictId?.let { originDistrictId = it }
        employeeUpdate.originAddress?.let { originAddress = it }
        employeeUpdate.familyPhone?.let { familyPhone = it }
        employeeUpdate.hobby?.let { hobby = it }
        employeeUpdate.remark?.let { remark = it }
    }

}