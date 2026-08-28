package com.mao.department.repository

import com.mao.department.entity.DepartmentDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : CoroutineCrudRepository<DepartmentDo, Int>