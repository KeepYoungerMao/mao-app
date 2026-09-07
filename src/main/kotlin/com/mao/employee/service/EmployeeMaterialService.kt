package com.mao.employee.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.employee.entity.EmployeeMaterialAddQo
import com.mao.employee.entity.EmployeeMaterialUpdateQo
import com.mao.employee.entity.EmployeeMaterialVo
import com.mao.employee.mapper.EmployeeMaterialCreateMapper
import com.mao.employee.mapper.EmployeeMaterialMapper
import com.mao.employee.mapper.EmployeeMaterialViewMapper
import com.mao.employee.repository.EmployeeMaterialRepository
import com.mao.employee.repository.EmployeeRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeMaterialService(
    private val employeeRepository: EmployeeRepository,
    private val employeeMaterialRepository: EmployeeMaterialRepository
) {

    suspend fun searchEmployeeMaterials(employeeId: Int?): List<EmployeeMaterialVo> {
        val employee = employeeRepository.findByIdOrThrow(employeeId)
        return employeeMaterialRepository.findAllByUserIdOrderByCreateTime(employee.id!!)
            .map(EmployeeMaterialViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createEmployeeMaterial(request: EmployeeMaterialAddQo): EmployeeMaterialVo {
        employeeRepository.findByIdOrThrow(request.employeeId)
        val material = employeeMaterialRepository.save(EmployeeMaterialCreateMapper.map(request))
        return EmployeeMaterialViewMapper.map(material)
    }

    @Transactional
    suspend fun updateEmployeeMaterial(request: EmployeeMaterialUpdateQo): EmployeeMaterialVo {
        val material = employeeMaterialRepository.findById(
            request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        ) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = employeeMaterialRepository.save(
            EmployeeMaterialMapper.copyToExistDo(request, material)
        )
        return EmployeeMaterialViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteEmployeeMaterial(id: Int?): Tips {
        val materialId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        employeeMaterialRepository.findById(materialId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        employeeMaterialRepository.deleteById(materialId)
        return Tips("数据删除成功")
    }

}