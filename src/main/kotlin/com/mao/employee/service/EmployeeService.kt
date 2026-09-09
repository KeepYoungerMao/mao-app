package com.mao.employee.service

import com.mao.common.entity.ErrorCode
import com.mao.common.entity.PageResponse
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.common.util.currentUser
import com.mao.department.repository.DepartmentRepository
import com.mao.employee.entity.*
import com.mao.employee.handler.EmployeeCodeHandler
import com.mao.employee.mapper.EmployeeCreateMapper
import com.mao.employee.mapper.EmployeeDepartmentCreateMapper
import com.mao.employee.mapper.EmployeeMapper
import com.mao.employee.mapper.EmployeeViewMapper
import com.mao.employee.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class EmployeeService(
    private val employeeCodeHandler: EmployeeCodeHandler,
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
    private val employeeDepartmentRefRepository: EmployeeDepartmentRefRepository,
    private val employeeEducationRepository: EmployeeEducationRepository,
    private val employeeWorkRepository: EmployeeWorkRepository,
    private val employeeRelationshipRepository: EmployeeRelationshipRepository,
    private val employeeMaterialRepository: EmployeeMaterialRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun searchEmployees(request: EmployeeQo): PageResponse<EmployeeVo> {
        return employeeRepository.page(request).map {
            EmployeeViewMapper.map(it)
        }
    }

    @Transactional
    suspend fun createEmployee(request: EmployeeAddQo): EmployeeVo {
        val employee = EmployeeCreateMapper.map(request).apply {
            this.employeeCode = employeeCodeHandler.generateEmployeeCode(request.departmentId!!)
        }
        val savedEmployee = employeeRepository.save(employee)
        return EmployeeViewMapper.map(savedEmployee)
    }

    @Transactional
    suspend fun updateEmployee(employeeUpdate: EmployeeUpdateQo): EmployeeVo {
        val employee = employeeRepository.findByIdOrThrow(employeeUpdate.id)
        val employeeUpdateDo = EmployeeMapper.copyToExistDo(employeeUpdate, employee)
        val updatedEmployee = employeeRepository.save(employeeUpdateDo)
        return EmployeeViewMapper.map(updatedEmployee)
    }

    /**
     * 新增用户部门关联
     */
    @Transactional
    suspend fun createEmployeeDepartment(request: EmployeeDepartmentAddQo): Tips {
        val employeeId = request.employeeId ?: throw AppException(ErrorCode.BAD_REQUEST)
        val departmentId = request.departmentId ?: throw AppException(ErrorCode.BAD_REQUEST)

        // 检验部门是否存在
        val department = departmentRepository.findById(departmentId) ?: throw AppException(ErrorCode.BAD_REQUEST, "部门不存在")
        // 检验部门是否允许分配成员
        if (department.memberAssignable != true) {
            throw AppException(ErrorCode.BAD_REQUEST, "该部门不允许分配成员")
        }
        // 检验日期是否合法
        validateDepartmentDate(request.startDate, request.endDate)
        // 检验用户是否已关联该部门
        if (employeeDepartmentRefRepository.findByEmployeeIdAndDepartmentId(employeeId, departmentId) != null) {
            throw AppException(ErrorCode.BAD_REQUEST, "用户已关联该部门")
        }
        // 如果新增的是主职，先清除该用户已有部门的主职状态
        if (request.primaryAssignment == true) {
            employeeDepartmentRefRepository.clearPrimaryAssignment(employeeId)
        }
        // 保存数据
        employeeDepartmentRefRepository.save(EmployeeDepartmentCreateMapper.map(request))
        return Tips("用户部门关联创建成功")
    }

    /** 更新基础字段、主职状态或启用状态。 */
    @Transactional
    suspend fun updateUserDepartment(request: EmployeeDepartmentUpdateQo): Tips {
        val id = request.id ?: throw AppException(ErrorCode.BAD_REQUEST)
        // 查询当前关联数据
        val current = employeeDepartmentRefRepository.findById(id) ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        // 检验日期是否合法
        if (request.startDate != null) {
            current.startDate = request.startDate
        }
        if (request.endDate != null) {
            current.endDate = request.endDate
        }
        validateDepartmentDate(current.startDate, current.endDate)
        // 检验职务ID
        if (request.positionId != null) {
            current.positionId = request.positionId
        }
        // 处理主职状态
        if (request.primaryAssignment != null) {
            current.primaryAssignment = request.primaryAssignment
            if (request.primaryAssignment) {
                employeeDepartmentRefRepository.clearPrimaryAssignment(current.employeeId!!)
            }
        }
        // 处理启用状态
        if (request.enabled != null) {
            current.enabled = request.enabled
            if (current.enabled == false && employeeDepartmentRefRepository.countByEmployeeIdAndEnabled(current.employeeId!!, true) <= 1L) {
                throw AppException(ErrorCode.BAD_REQUEST, "用户至少需要一个启用的关联部门")
            }
        }
        // 保存数据
        employeeDepartmentRefRepository.save(current)
        return Tips("用户部门关联更新成功")
    }

    private fun validateDepartmentDate(startDate: LocalDate?, endDate: LocalDate?) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw AppException(ErrorCode.BAD_REQUEST, "结束日期不能早于开始日期")
        }
    }

    @Transactional
    suspend fun deleteEmployee(id: Int?) : Tips {
        val operateUser = currentUser()
        val employee = employeeRepository.findByIdOrThrow(id)
        log.info("user: [{}] operate: delete employee [{}]", operateUser, employee.employeeCode)
        employeeRepository.deleteById(employee.id!!)
        // delete employee_education
        val count1 = employeeEducationRepository.deleteByEmployeeId(employee.id!!)
        log.info("user: [{}] operate: delete employee education [count: {}]", operateUser, count1)
        // delete employee_work
        val count2 = employeeWorkRepository.deleteByEmployeeId(employee.id!!)
        log.info("user: [{}] operate: delete employee work [count: {}]", operateUser, count2)
        // delete employee_relationship
        val count3 = employeeRelationshipRepository.deleteByEmployeeId(employee.id!!)
        log.info("user: [{}] operate: delete employee relationship [count: {}]", operateUser, count3)
        // delete employee_material
        val count4 = employeeMaterialRepository.deleteByEmployeeId(employee.id!!)
        log.info("user: [{}] operate: delete employee material [count: {}]", operateUser, count4)
        // delete employ_department
        val count5 = employeeDepartmentRefRepository.deleteByEmployeeId(employee.id!!)
        log.info("user: [{}] operate: delete employee department [count: {}]", operateUser, count5)
        return Tips("数据删除成功")
    }

    @Transactional
    suspend fun unbindUser(employeeId: Int?) : Tips {
        val employee = employeeRepository.findByIdOrThrow(employeeId)
        log.info("employee [{}] will unbind user", employee.employeeCode!!)
        val count = employeeRepository.unbindUserById(employee.id!!)
        log.info("employee [{}] unbind count: {}", employeeId, count)
        return Tips("用户解绑成功")
    }

}
