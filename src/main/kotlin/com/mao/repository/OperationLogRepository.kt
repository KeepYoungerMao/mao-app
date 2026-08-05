package com.mao.repository

import com.mao.entity.domain.OperationLogDo
import com.mao.entity.query.OperationLogQo

interface OperationLogRepository: BaseRepository<OperationLogDo, Long, OperationLogQo>