package com.mao.metric.repository

import com.mao.metric.entity.ServerMetric
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServerMetricRepository: CoroutineCrudRepository<ServerMetric, Long>