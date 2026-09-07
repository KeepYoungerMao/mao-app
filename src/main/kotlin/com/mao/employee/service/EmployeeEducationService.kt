package com.mao.employee.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.employee.entity.EmployeeEducationAddQo
import com.mao.employee.entity.EmployeeEducationUpdateQo
import com.mao.employee.entity.EmployeeEducationVo
import com.mao.employee.mapper.EmployeeEducationCreateMapper
import com.mao.employee.mapper.EmployeeEducationMapper
import com.mao.employee.mapper.EmployeeEducationViewMapper
import com.mao.employee.repository.EmployeeEducationRepository
import com.mao.employee.repository.EmployeeRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeEducationService(
    private val employeeRepository: EmployeeRepository,
    private val employeeEducationRepository: EmployeeEducationRepository
){

    suspend fun searchEmployeeEducations(employeeId: Int?): List<EmployeeEducationVo> {
        val employee = employeeRepository.findByIdOrThrow(employeeId)
        return employeeEducationRepository.findAllByEmployeeIdOrderByStartDate(employee.id!!)
            .map(EmployeeEducationViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createEmployeeEducation(request: EmployeeEducationAddQo): EmployeeEducationVo {
        employeeRepository.findByIdOrThrow(request.employeeId)
        val education = employeeEducationRepository.save(EmployeeEducationCreateMapper.map(request))
        return EmployeeEducationViewMapper.map(education)
    }

    @Transactional
    suspend fun updateEmployeeEducation(request: EmployeeEducationUpdateQo): EmployeeEducationVo {
        val education = employeeEducationRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = employeeEducationRepository.save(
            EmployeeEducationMapper.copyToExistDo(request, education)
        )
        return EmployeeEducationViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteEmployeeEducation(id: Int?): Tips {
        val educationId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        employeeEducationRepository.findById(educationId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        employeeEducationRepository.deleteById(educationId)
        return Tips("数据删除成功")
    }

}