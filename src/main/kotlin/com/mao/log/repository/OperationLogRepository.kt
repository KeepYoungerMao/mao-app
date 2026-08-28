package com.mao.log.repository

import com.mao.common.repository.BaseRepository
import com.mao.log.entity.OperationLogDo
import com.mao.log.entity.OperationLogQo
import org.springframework.stereotype.Repository

@Repository
interface OperationLogRepository: BaseRepository<OperationLogDo, Long, OperationLogQo>