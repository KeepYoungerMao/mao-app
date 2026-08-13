package com.mao.repository

import com.mao.entity.ServerMetric
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServerMetricRepository: CoroutineCrudRepository<ServerMetric, Long>