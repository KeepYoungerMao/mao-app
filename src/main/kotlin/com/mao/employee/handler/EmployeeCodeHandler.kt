package com.mao.employee.handler

import com.mao.common.config.ZONE_SHANGHAI
import com.mao.common.entity.ErrorCode
import com.mao.common.ex.AppException
import com.mao.department.entity.DepartmentCode
import com.mao.department.entity.DepartmentDo
import com.mao.department.repository.DepartmentRepository
import com.mao.employee.entity.EmployeeCodeSequenceDo
import com.mao.employee.repository.EmployeeCodeSequenceRepository
import kotlinx.coroutines.delay
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

/**
 * ## 员工编码处理器
 */
@Service
class EmployeeCodeHandler(
    private val departmentRepository: DepartmentRepository,
    private val employeeCodeSequenceRepository: EmployeeCodeSequenceRepository,
    transactionManager: ReactiveTransactionManager
) {

    private companion object {
        /** 并发冲突时的最大重试次数，不代表序列号生成次数。 */
        const val MAX_ATTEMPTS = 10
    }

    private val transactionOperator = TransactionalOperator.create(
        transactionManager,
        DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
    )

    /**
     * ## 生成 员工编码
     * 代码通过：唯一约束 + 乐观锁 + 独立事务 的组合，实现并发安全的不重复员工编码生成
     * 序列更新使用独立事务提交，即使外层业务失败，已分配的序列号也不会回滚
     * 生成员工编码：两位年份 + 六位三级部门代码 + 至少四位的部门年度序列号。
     * 序列更新使用独立事务提交，员工保存失败不会回滚已经分配的序列号。
    */
    suspend fun generateEmployeeCode(departmentId: Int): String {
        val (first, second, third) = getDepartmentThreeLevelCode(departmentId)
        val departmentCode = first + second + third
        val year = LocalDate.now(ZONE_SHANGHAI).year
        val sequence = nextSequence(year, departmentCode)
        val yearCode = (year % 100).toString().padStart(2, '0')
        return yearCode + departmentCode + sequence.toString().padStart(4, '0')
    }

    private suspend fun nextSequence(year: Int, departmentCode: String): Int {
        repeat(MAX_ATTEMPTS) {
            try {
                transactionOperator.executeAndAwait {
                    allocateSequenceOnce(year, departmentCode)
                }?.let { return it }
                // 乐观锁更新失败：重新读取当前值后重试。
            } catch (_: DataIntegrityViolationException) {
                // 首次并发创建时唯一键冲突：本次事务回滚后重试。
                delay(2.milliseconds)
            }
        }
        throw AppException(ErrorCode.DB_ERROR, "员工编码序列更新失败，请稍后重试")
    }

    private suspend fun allocateSequenceOnce(year: Int, departmentCode: String): Int? {
        val current = employeeCodeSequenceRepository.findBySequenceYearAndDepartmentCode(year, departmentCode)
        if (current == null) {
            return employeeCodeSequenceRepository.save(
                EmployeeCodeSequenceDo(sequenceYear = year, departmentCode = departmentCode, currentValue = 1)
            ).currentValue
        } else {
            val id = current.id ?: throw AppException(ErrorCode.DB_ERROR, "序列记录 ID 缺失")
            val currentValue = current.currentValue ?: throw AppException(ErrorCode.DB_ERROR, "序列号数据异常")
            val nextValue = currentValue + 1
            return nextValue.takeIf {
                employeeCodeSequenceRepository.updateCurrentValue(id, currentValue, it) == 1
            }
        }
    }

    /**
     * 获取指定部门用于员工编号的三层部门代码。
     *
     * 只有部门处于启用状态且允许分配成员时才允许获取。沿当前部门向上查找父级：
     * 无父级返回 `00,00,当前部门代码`；只有一个父级返回 `00,父级代码,当前部门代码`；
     * 有两个及以上父级时取从最顶级开始的前三层代码，超过三层的更深层级忽略。
     * 父级记录不存在时，按已查找到的实际层级计算；检测到循环引用时拒绝返回。
     */
    suspend fun getDepartmentThreeLevelCode(departmentId: Int): DepartmentCode {
        val department = departmentRepository.findById(departmentId)
            ?: throw AppException(ErrorCode.DATA_NOT_FOUND)
        if (department.status != 1 || department.memberAssignable != true) {
            throw AppException(ErrorCode.OPERATION_NOT_ALLOWED, "部门未启用或不允许分配成员")
        }

        val path = mutableListOf<DepartmentDo>()
        val visited = mutableSetOf<Int>()
        var current: DepartmentDo? = department
        while (current != null) {
            val currentId = current.id ?: break
            if (!visited.add(currentId)) {
                throw AppException(ErrorCode.BAD_REQUEST, "部门层级存在循环引用")
            }
            path += current
            val parentId = current.parentId ?: break
            current = departmentRepository.findById(parentId)
        }

        val codes = path.asReversed().map {
            it.departmentCode ?: throw AppException(ErrorCode.DATA_NOT_FOUND, "部门编号不存在")
        }
        if (codes.isEmpty()) {
            throw AppException(ErrorCode.DATA_NOT_FOUND, "部门编号不存在")
        }
        val selected = when (codes.size) {
            1 -> listOf("00", "00", codes[0])
            2 -> listOf("00", codes[0], codes[1])
            else -> codes.take(3)
        }
        return DepartmentCode(selected[0], selected[1], selected[2])
    }

}
