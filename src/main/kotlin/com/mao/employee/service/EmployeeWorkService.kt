package com.mao.employee.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.employee.entity.EmployeeWorkAddQo
import com.mao.employee.entity.EmployeeWorkUpdateQo
import com.mao.employee.entity.EmployeeWorkVo
import com.mao.employee.mapper.EmployeeWorkCreateMapper
import com.mao.employee.mapper.EmployeeWorkMapper
import com.mao.employee.mapper.EmployeeWorkViewMapper
import com.mao.employee.repository.EmployeeRepository
import com.mao.employee.repository.EmployeeWorkRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeWorkService(
    private val employeeRepository: EmployeeRepository,
    private val employeeWorkRepository: EmployeeWorkRepository
) {

    suspend fun searchEmployeeWorks(employeeId: Int?): List<EmployeeWorkVo> {
        val employee = employeeRepository.findByIdOrThrow(employeeId)
        return employeeWorkRepository.findAllByEmployeeIdOrderByStartDate(employee.id!!)
            .map(EmployeeWorkViewMapper::map)
            .toList()
    }

    @Transactional
    suspend fun createEmployeeWork(request: EmployeeWorkAddQo): EmployeeWorkVo {
        employeeRepository.findByIdOrThrow(request.employeeId)
        val work = employeeWorkRepository.save(EmployeeWorkCreateMapper.map(request))
        return EmployeeWorkViewMapper.map(work)
    }

    @Transactional
    suspend fun updateEmployeeWork(request: EmployeeWorkUpdateQo): EmployeeWorkVo {
        val work = employeeWorkRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val updated = employeeWorkRepository.save(EmployeeWorkMapper.copyToExistDo(request, work))
        return EmployeeWorkViewMapper.map(updated)
    }

    @Transactional
    suspend fun deleteEmployeeWork(id: Int?): Tips {
        val workId = id ?: throw AppException(ErrorCode.BAD_REQUEST)
        employeeWorkRepository.findById(workId) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        employeeWorkRepository.deleteById(workId)
        return Tips("数据删除成功")
    }

}