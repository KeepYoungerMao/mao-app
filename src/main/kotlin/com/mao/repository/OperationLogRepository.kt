package com.mao.repository

import com.mao.entity.OperationLogDo
import com.mao.entity.OperationLogQo
import org.springframework.stereotype.Repository

@Repository
interface OperationLogRepository: BaseRepository<OperationLogDo, Long, OperationLogQo>