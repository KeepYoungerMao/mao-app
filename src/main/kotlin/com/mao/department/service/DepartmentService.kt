package com.mao.department.service

import com.mao.common.entity.DataStatus
import com.mao.common.entity.ErrorCode
import com.mao.common.entity.Tips
import com.mao.common.ex.AppException
import com.mao.common.util.TreeUtils
import com.mao.department.entity.*
import com.mao.department.mapper.DepartmentCreateMapper
import com.mao.department.mapper.DepartmentMapper
import com.mao.department.mapper.DepartmentViewMapper
import com.mao.department.repository.DepartmentRepository
import com.mao.employee.repository.EmployeeDepartmentRefRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val employeeDepartmentRefRepository: EmployeeDepartmentRefRepository
) {

    suspend fun getDepartmentTree(): List<DepartmentVo> {
        val departments = departmentRepository.findAll().toList()
            .sortedWith(compareBy<DepartmentDo> { it.parentId ?: 0 }.thenBy { it.sortOrder ?: 0 }.thenBy { it.id ?: 0 })
            .map(DepartmentViewMapper::map)
        return TreeUtils.buildTree(departments)
    }

    @Transactional
    suspend fun createDepartment(request: DepartmentAddQo): DepartmentVo {
        val departmentCode = request.departmentCode ?: throw AppException(ErrorCode.BAD_REQUEST)
        if (departmentRepository.findByDepartmentCode(departmentCode) != null) {
            throw AppException(ErrorCode.BAD_REQUEST, "部门编码已存在")
        }

        val parent = request.parentId?.let { parentId ->
            departmentRepository.findById(parentId)
                ?: throw AppException(ErrorCode.DATA_NOT_FOUND, "数据错误：父级部门不存在")
        }
        val departmentType = inferDepartmentType(parent)
        val department = DepartmentCreateMapper.map(request).apply {
            // The hierarchy determines the type; callers cannot choose it.
            this.departmentType = departmentType
            status = DataStatus.ENABLED.status
            memberAssignable = request.memberAssignable ?: false
            sortOrder = request.sortOrder ?: 0
        }
        return DepartmentViewMapper.map(departmentRepository.save(department))
    }

    @Transactional
    suspend fun updateDepartment(request: DepartmentUpdateQo): DepartmentVo {
        val current = departmentRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        return DepartmentViewMapper.map(departmentRepository.save(DepartmentMapper.copyToExistDo(request, current)))
    }

    @Transactional
    suspend fun updateDepartmentStatus(request: DepartmentStatusUpdateQo): Tips {
        val department = departmentRepository.findById(request.id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        val status = request.status ?: throw AppException(ErrorCode.BAD_REQUEST)
        val hasEnabledChild = department.id?.let { parentId ->
            departmentRepository.findByParentId(parentId).toList().any { child -> child.status == DataStatus.ENABLED.status }
        } ?: false
        if (status != DataStatus.ENABLED.status && hasEnabledChild) {
            throw AppException(ErrorCode.OPERATION_NOT_ALLOWED, "该部门有子级部门正常使用，不能禁用或归档")
        }
        if (status != DataStatus.ENABLED.status && employeeDepartmentRefRepository.countByDepartmentId(department.id!!) > 0) {
            throw AppException(ErrorCode.OPERATION_NOT_ALLOWED, "该部门有员工绑定，不能禁用或归档")
        }
        department.status = status
        departmentRepository.save(department)
        return Tips("部门状态更新成功")
    }

    @Transactional
    suspend fun updateDepartmentSort(request: List<DepartmentSortUpdateQo>): Tips {
        if (request.isEmpty()) {
            throw AppException(ErrorCode.BAD_REQUEST)
        }
        val ids = request.map { it.id ?: throw AppException(ErrorCode.BAD_REQUEST) }
        if (ids.size != ids.toSet().size) {
            throw AppException(ErrorCode.BAD_REQUEST, "部门重复")
        }
        val departments = ids.map { id ->
            departmentRepository.findById(id) ?: throw AppException(ErrorCode.DATA_NOT_FOUND, "部门不存在")
        }
        val parentIds = departments.map { it.parentId }.toSet()
        if (parentIds.size != 1) {
            throw AppException(ErrorCode.BAD_REQUEST, "同级部门下才可排序")
        }
        departments.forEachIndexed { index, department ->
            department.sortOrder = request[index].sortOrder
        }
        departmentRepository.saveAll(departments).toList()
        return Tips("部门排序成功")
    }

    @Transactional
    suspend fun deleteDepartment(id: Int?): Tips {
        val department = departmentRepository.findById(id ?: throw AppException(ErrorCode.BAD_REQUEST))
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        if (employeeDepartmentRefRepository.countByDepartmentId(department.id!!) > 0) {
            throw AppException(ErrorCode.OPERATION_NOT_ALLOWED, "该部门有员工绑定，不能删除")
        }
        if (departmentRepository.countByParentId(department.id!!) > 0) {
            throw AppException(ErrorCode.OPERATION_NOT_ALLOWED, "该部门包含子级部门，无法删除")
        }
        departmentRepository.deleteById(department.id!!)
        return Tips("部门删除成功")
    }

    private fun inferDepartmentType(parent: DepartmentDo?): Int {
        if (parent == null) {
            return DepartmentType.FIRST.code
        }
        return when (parent.departmentType) {
            DepartmentType.FIRST.code -> DepartmentType.SECOND.code
            DepartmentType.SECOND.code -> DepartmentType.THIRD.code
            DepartmentType.THIRD.code -> DepartmentType.FOURTH.code
            DepartmentType.FOURTH.code -> DepartmentType.FIFTH.code
            DepartmentType.FIFTH.code -> throw AppException(
                ErrorCode.BAD_REQUEST,
                "该部门下不能再创建子级部门"
            )
            null -> throw AppException(ErrorCode.BAD_REQUEST, "父级部门缺失")
            else -> throw AppException(ErrorCode.BAD_REQUEST, "非法部门类型")
        }
    }
}
