package com.mao.repository

import com.mao.entity.DepartmentDo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : CoroutineCrudRepository<DepartmentDo, Int>