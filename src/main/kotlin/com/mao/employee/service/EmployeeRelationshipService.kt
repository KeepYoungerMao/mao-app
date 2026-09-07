package com.mao.employee.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.employee.entity.EmployeeRelationshipAddQo
import com.mao.employee.entity.EmployeeRelationshipUpdateQo
import com.mao.employee.entity.EmployeeRelationshipVo
import com.mao.employee.mapper.EmployeeRelationshipCreateMapper
import com.mao.employee.mapper.EmployeeRelationshipMapper
import com.mao.employee.mapper.EmployeeRelationshipViewMapper
import com.mao.employee.repository.EmployeeRelationshipRepository
import com.mao.employee.repository.EmployeeRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeRelationshipService(
    private val employeeRepository: EmployeeRepository,
    private val employeeRelationshipRepository: EmployeeRelationshipRepository
) {

    suspend fun searchEmployeeRelationships(employeeId: Int?): List<EmployeeRelationshipVo> {
        val employee = employeeRepository.findByIdOrThrow(employeeId)
        return employeeRelationshipRepository.findAllByEmployeeIdOrderByCreateTime(employee.id!!)
            .map(EmployeeRelationshipViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createEmployeeRelationship(request: EmployeeRelationshipAddQo): EmployeeRelationshipVo {
        employeeRepository.findByIdOrThrow(request.employeeId)
        val relationship = employeeRelationshipRepository.save(EmployeeRelationshipCreateMapper.map(request))
        return EmployeeRelationshipViewMapper.map(relationship)
    }

    @Transactional
    suspend fun updateEmployeeRelationship(request: EmployeeRelationshipUpdateQo): EmployeeRelationshipVo {
        val relationship = employeeRelationshipRepository.findById(
            request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        ) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = employeeRelationshipRepository.save(
            EmployeeRelationshipMapper.copyToExistDo(request, relationship)
        )
        return EmployeeRelationshipViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteEmployeeRelationship(id: Int?): Tips {
        val relationshipId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        employeeRelationshipRepository.findById(relationshipId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        employeeRelationshipRepository.deleteById(relationshipId)
        return Tips("数据删除成功")
    }

}